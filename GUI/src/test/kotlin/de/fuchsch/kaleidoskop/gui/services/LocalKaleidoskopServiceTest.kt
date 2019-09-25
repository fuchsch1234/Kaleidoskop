package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Tag
import javafx.scene.image.Image
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.io.FileMatchers.anExistingDirectory
import org.junit.jupiter.api.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class LocalKaleidoskopServiceTest {

    private val baseDir = createTempDir()

    // Instantiate service lazily to ensure the services parses the directory only after setUp completed.
    private val service : LocalKaleidoskopService by lazy { LocalKaleidoskopService(baseDir.absolutePath) }

    @BeforeEach
    fun setUp() {
        // Create tag directories
        File(baseDir, "1_test").mkdirs()
        File(baseDir, "2_foo").mkdirs()
        File(baseDir, "3_bar").mkdirs()
        // Create mock files
        File(baseDir, "data").mkdirs()
        val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        ImageIO.write(image, "jpg", File(baseDir, "data/1_1.jpg"))
        ImageIO.write(image, "jpg", File(baseDir, "data/2_2.jpg"))
        ImageIO.write(image, "jpg", File(baseDir, "data/3_3.jpg"))
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

    @Test
    fun `getAllImages returns all images`() {
        val images = service.getAllImages().blockingSingle()
        assertThat(images, hasSize(3))
        assertThat(images, hasItems(
            hasProperty("name", equalTo("1.jpg")),
            hasProperty("name", equalTo("2.jpg")),
            hasProperty("name", equalTo("3.jpg"))
        ))
    }

}