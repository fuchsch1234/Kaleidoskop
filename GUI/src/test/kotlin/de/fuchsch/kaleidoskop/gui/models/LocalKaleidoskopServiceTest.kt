package de.fuchsch.kaleidoskop.gui.models

import de.fuchsch.kaleidoskop.gui.services.LocalKaleidoskopService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocalKaleidoskopServiceTest {

    private val baseDir = createTempDir()

    private val serviceTest = LocalKaleidoskopService(baseDir.absolutePath)

    @BeforeAll
    fun setUp() {
        File(baseDir, "1_test").mkdirs()
        File(baseDir, "2_foo").mkdirs()
        File(baseDir, "3_bar").mkdirs()
    }

    @AfterAll
    fun cleanUp() {
        baseDir.deleteRecursively()
    }

    @Test
    fun `getAllTags returns all tags`() {
        val tags = serviceTest.getAllTags().blockingFirst()
        assertThat(tags, hasSize(3))
        assertThat(tags, hasItems(
            hasProperty("name", equalTo("test")),
            hasProperty("name", equalTo("foo")),
            hasProperty("name", equalTo("bar"))
        ))
    }

}