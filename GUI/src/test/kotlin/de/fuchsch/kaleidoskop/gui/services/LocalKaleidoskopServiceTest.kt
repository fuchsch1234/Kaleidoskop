package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Tag
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.io.FileMatchers.anExistingDirectory
import org.junit.jupiter.api.*
import java.io.File

class LocalKaleidoskopServiceTest {

    private val baseDir = createTempDir()

    private val service = LocalKaleidoskopService(baseDir.absolutePath)

    @BeforeEach
    fun setUp() {
        File(baseDir, "1_test").mkdirs()
        File(baseDir, "2_foo").mkdirs()
        File(baseDir, "3_bar").mkdirs()
    }

    @AfterEach
    fun cleanUp() {
        baseDir.deleteRecursively()
    }

    @Test
    fun `getAllTags returns all tags`() {
        val tags = service.getAllTags().blockingSingle()
        assertThat(tags, hasSize(3))
        assertThat(tags, hasItems(
            hasProperty("name", equalTo("test")),
            hasProperty("name", equalTo("foo")),
            hasProperty("name", equalTo("bar"))
        ))
    }

    @Test
    fun `createTag creates a new tag folder`() {
        val newTag = service.createTag(Tag(0, "foobar")).blockingSingle()
        val tagDir = File(baseDir, "${newTag.id}_${newTag.name}")
        assertThat(tagDir, `is`(anExistingDirectory()))
    }

    @Test
    fun `every Tag has a different id`() {
        val newTags = listOf(
            service.createTag(Tag(0, "foobar")).blockingSingle(),
            service.createTag(Tag(0, "foobar")).blockingSingle())

        // Both tags were successfully created
        assertThat(newTags, hasSize(2))
        // The tags have different ids
        assertThat(newTags[0].id, not(equalTo(newTags[1].id)))
    }

}