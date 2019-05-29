package de.fuchsch.kaleidoskop.gui.models

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ImageTest {

    private val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Test
    fun `deserializing image without tags works`() {
        val json = """
            {
                "id": 1,
                "name": "foo",
                "mimeType": "bar"
            }
        """.trimIndent()
        val image = mapper.readValue(json, Image::class.java)
        assertEquals(image.id, 1)
        assertEquals(image.name, "foo")
        assertThat(image.tags.size, `is`(0))
    }

    @Test
    fun `deserializing image with tags works`() {
        val json = """
            {
                "id": 1,
                "name": "foo",
                "mimeType": "bar",
                "tags": [ { "id": 1, "name": "0" } ]
            }
        """.trimIndent()
        val image = mapper.readValue(json, Image::class.java)
        assertEquals(image.id, 1)
        assertEquals(image.name, "foo")
        assertThat(image.tags.size, `is`(1))
    }
}