package de.fuchsch.kaleidoskop.server

import de.fuchsch.kaleidoskop.server.model.ImageDAO
import de.fuchsch.kaleidoskop.server.model.TagDAO
import de.fuchsch.kaleidoskop.server.repositories.ImageRepository
import de.fuchsch.kaleidoskop.server.repositories.TagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
class ImageRepositoryTest (
    @Autowired val imageRepository: ImageRepository,
    @Autowired val tagRepository: TagRepository
) {

    private val testImageData = ByteArray(255) { it.toByte() }

    private val mimeType = "application/binary"

    private val tags = listOf(
        TagDAO(0, "0", emptyList()),
        TagDAO(0, "1", emptyList())
    )

    @Test
    @Transactional
    fun `basic insert`() {
        val name = "0.jpg"
        val image = ImageDAO( 0, name, mimeType, emptyList(), testImageData )
        assertThat(imageRepository.findAll()).hasSize(0)
        imageRepository.save(image)
        assertThat(imageRepository.findAll())
            .hasSize(1)
            .allMatch { it.name == name && it.data == testImageData }
    }

    @Test
    @Transactional
    fun `insert with tags`() {
        val image = ImageDAO(0, "0", mimeType, listOf(tags[0]), testImageData)
        imageRepository.save(image)
        assertThat(imageRepository.findByTagsName("0")).hasSize(1).allMatch { it.name == "0" }
        assertThat(tagRepository.findAll()).hasSize(1).allMatch { it.name == "0" }
        tagRepository.saveAll(tags)
        assertThat(tagRepository.findAll()).hasSize(2)
        assertThat(imageRepository.findByTagsName("0")).hasSize(1).allMatch { it.name == "0" }
        assertThat(imageRepository.findByTagsName("1")).hasSize(0)
    }

    @Test
    @Transactional
    fun `removing does not remove tags`() {
        val image = ImageDAO(0, "0", mimeType, listOf(tags[0]), testImageData)
        imageRepository.save(image)
        assertThat(imageRepository.findAll()).hasSize(1)
        assertThat(tagRepository.findAll()).hasSize(1).allMatch { it.name == "0" }
        imageRepository.delete(image)
        assertThat(imageRepository.findAll()).hasSize(0)
        assertThat(tagRepository.findAll()).hasSize(1).allMatch { it.name == "0" }
    }

}
