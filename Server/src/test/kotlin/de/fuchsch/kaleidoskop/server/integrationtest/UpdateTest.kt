package de.fuchsch.kaleidoskop.server.integrationtest

import com.fasterxml.jackson.databind.ObjectMapper
import de.fuchsch.kaleidoskop.server.model.ImageUpdateDTO
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
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
        MockMvcRequestBuilders.multipart("/images")
            .file(MockMultipartFile("file", filename, "text/plain", ByteArray(255)))

    @Test
    fun `updating tags has no side effects`() {
        mvc.perform(uploadFileRequest("filename1.txt")).andExpect(status().isCreated)
        mvc.perform(uploadFileRequest("filename2.txt")).andExpect(status().isCreated)

        val patchDTO = ImageUpdateDTO(id=null, name=null, mimeType=null, tags=tags)
        mvc.perform(patch("/images/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(patchDTO))
        ).andExpect(status().is2xxSuccessful)
        mvc.perform(patch("/images/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(patchDTO))
        ).andExpect(status().is2xxSuccessful)

        val patchDTO2 = ImageUpdateDTO(id=null, name=null, mimeType=null, tags=listOf(tags[0]))
        mvc.perform(patch("/images/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(patchDTO2))
        ).andExpect(status().is2xxSuccessful)
        mvc.perform(get("/images/1"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.tags[*].name", `is`(listOf("0"))))
        mvc.perform(get("/images/2"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.tags[*].name", `is`(listOf("0", "1"))))
    }
}