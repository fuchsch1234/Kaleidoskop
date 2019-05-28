package de.fuchsch.kaleidoskop.server.integrationtest

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UploadTest (
    @Autowired val mvc: MockMvc
) {

    fun uploadFileRequest(filename: String) =
        MockMvcRequestBuilders.multipart("/api/v1/images")
            .file(MockMultipartFile("file", filename, "text/plain", ByteArray(255)))

    @Test
    fun `simple upload`() {
        val result = mvc.perform(uploadFileRequest("filename1.jpg"))
            .andExpect(status().isCreated)
            .andReturn()
        val location = result.response.getHeader("Location") ?: fail("Location header missing")
        mvc.perform(get(location)).andExpect(status().is2xxSuccessful)
    }

    @Test
    fun `repeated upload fails`() {
        mvc.perform(uploadFileRequest("upload1.jpg")).andExpect(status().isCreated)
        mvc.perform(uploadFileRequest("upload1.jpg")).andExpect(status().isConflict)
    }

}