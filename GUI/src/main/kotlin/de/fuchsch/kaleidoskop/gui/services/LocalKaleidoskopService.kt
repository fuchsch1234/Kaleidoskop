package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Tag
import io.reactivex.Observable
import javafx.embed.swing.SwingFXUtils
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicLong
import javax.imageio.ImageIO

class LocalKaleidoskopService(private val basePath: String): KaleidoskopService {

    private val TAG_REGEX = Regex("""(\d+)_(.*)""")

    private val IMAGE_REGEX = Regex("""(\d+)_(.*)""")

    private val nextTagId = AtomicLong(0)

    private val images = mutableListOf<Image>()

    private val tags = mutableListOf<Tag>()

    init {
        tags.addAll(File(basePath).walk()
            .filter { it.isDirectory }
            .filter { TAG_REGEX.matches(it.name) }
            .map { file ->
                TAG_REGEX.find(file.name)?.let {
                    Tag(it.groupValues[1].toLong(), it.groupValues[2])
                } ?: throw RuntimeException("Found tag folder not matching the expected naming convention")
            })
        images.addAll(File(basePath, "data").walk()
            .filter { it.isFile }
            .map { file ->
                IMAGE_REGEX.find(file.name)?.let {
                    val image = Image(it.groupValues[1].toLong(), it.groupValues[2])
                    image.image = SwingFXUtils.toFXImage(ImageIO.read(file), null)
                    image
                } ?: throw RuntimeException("Found file not matching the expecting image naming convention in data folder")
            })
    }

    override fun getAllTags(): Observable<List<Tag>> = Observable.just(tags)

    private fun findNextFreeTagId(): Long {
        while (Files.newDirectoryStream(Paths.get(basePath), "${nextTagId.addAndGet(1)}_*").count() > 0) { }
        return nextTagId.get()
    }

    override fun createTag(tag: Tag): Observable<Tag> {
        val tag = Tag(id=findNextFreeTagId(), name=tag.name)
        File(basePath, "${tag.id}_${tag.name}").mkdirs()
        tags.add(tag)
        return Observable.just(tag)
    }

    override fun createImage(image: Image): Observable<Image> {
        throw NotImplementedError("Missing")
    }

    override fun getAllImages(): Observable<List<Image>> = Observable.just(images)

    override fun addTag(image: Image, tag: Tag): Observable<Image> {
        throw NotImplementedError("Missing")
    }

    override fun removeTag(image: Image, tag: Tag): Observable<Image> {
        throw NotImplementedError("Missing")
    }

}