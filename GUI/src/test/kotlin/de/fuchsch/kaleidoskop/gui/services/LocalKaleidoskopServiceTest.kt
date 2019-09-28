package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Tag
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.io.FileMatchers.anExistingDirectory
import org.junit.jupiter.api.*
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files.createSymbolicLink
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
        // Create tag symlinks
        createSymbolicLink(File(baseDir, "1_test/1").toPath(), File(baseDir, "data/1_1.jpg").toPath())
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

    @Test
    fun `images have correct tags`() {
        val images = service.getAllImages().blockingSingle()
        val image = images.first { it.id == 1L }
        assertThat(image.tags, hasSize(1))
        assertThat(image.tags, hasItem<Tag>(hasProperty("name", equalTo("test"))))
    }

    @Test
    fun `addTag adds a single tag to an image`() {
        val images = service.getAllImages().blockingSingle()
        val image = images.first { it.id == 2L }
        assertThat(image.tags, empty())
        val tag = service.getAllTags().blockingSingle().last()
        val imageWithTag = service.addTag(image, tag).blockingSingle()

        assertThat(imageWithTag.tags, hasSize(1))
        assertThat(imageWithTag.tags, hasItem(tag))
    }

    @Test
    fun `added tags are persisted`() {
        val image = service.getAllImages().blockingSingle().first { it.id == 2L }
        assertThat(image.tags, empty())
        val tag = service.getAllTags().blockingSingle().last()
        service.addTag(image, tag).blockingSingle()

        val newService = LocalKaleidoskopService(baseDir.absolutePath)
        val imageWithTag = newService.getAllImages().blockingSingle().first { it.id == 2L}

        assertThat(imageWithTag.tags, hasSize(1))
        assertThat(imageWithTag.tags, hasItem(tag))
    }

}