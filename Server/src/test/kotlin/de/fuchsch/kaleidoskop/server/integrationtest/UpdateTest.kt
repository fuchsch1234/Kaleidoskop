package de.fuchsch.kaleidoskop.server.integrationtest

import com.fasterxml.jackson.databind.ObjectMapper
import de.fuchsch.kaleidoskop.server.model.TagDAO
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UpdateTest (
    @Autowired val mvc: MockMvc
) {

    private val tags = listOf(
        TagDAO(1, "0", emptyList()),
        TagDAO(2, "1", emptyList())
    )

    private val mapper = ObjectMapper()

    fun uploadFileRequest(filename: String) =
        MockMvcRequestBuilders.multipart("/api/v1/images")
            .file(MockMultipartFile("file", filename, "text/plain", ByteArray(255)))

    @Test
    fun `updating tags has no side effects`() {
        mvc.perform(uploadFileRequest("filename1.txt")).andExpect(status().isCreated)
        mvc.perform(uploadFileRequest("filename2.txt")).andExpect(status().isCreated)

        mvc.perform(post("/api/v1/images/1/relationships/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(tags[0]))
        ).andExpect(status().is2xxSuccessful)
        mvc.perform(post("/api/v1/images/1/relationships/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(tags[1]))
        ).andExpect(status().is2xxSuccessful)
        mvc.perform(post("/api/v1/images/2/relationships/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(tags[0]))
        ).andExpect(status().is2xxSuccessful)
        mvc.perform(get("/api/v1/images/1"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.tags[*].name", `is`(listOf("0", "1"))))
        mvc.perform(get("/api/v1/images/2"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.tags[*].name", `is`(listOf("0"))))

        mvc.perform(delete("/api/v1/images/1/relationships/tags/0")
        ).andExpect(status().is2xxSuccessful)
        mvc.perform(get("/api/v1/images/1"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.tags[*].name", `is`(listOf("1"))))
        mvc.perform(get("/api/v1/images/2"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.tags[*].name", `is`(listOf("0"))))
    }
}