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

    private val nextImageId = AtomicLong(0)

    private val images = mutableMapOf<Long, Image>()

    private val tags = mutableMapOf<Long, Tag>()

    init {
        File(basePath).walk()
            .filter { it.isDirectory }
            .forEach { file ->
                TAG_REGEX.find(file.name)?.let {
                    val id = it.groupValues[1].toLong()
                    tags[id] = Tag(id, it.groupValues[2])
                }
            }
        File(basePath, "data").walk()
            .filter { it.isFile }
            .forEach { file ->
                IMAGE_REGEX.find(file.name)?.let {
                    val id = it.groupValues[1].toLong()
                    val image = Image(id, it.groupValues[2])
                    image.image = SwingFXUtils.toFXImage(ImageIO.read(file), null)
                    images[id] = image
                }
            }
        tags.forEach { (_, tag) ->
            File(basePath, "${tag.id}_${tag.name}").walk()
                .filterNot { it.isDirectory }
                .forEach { file ->
                    val id = file.name.toLong()
                    images[id]?.tags?.add(tag)
                }
        }
    }

    override fun getAllTags(): Observable<List<Tag>> = Observable.just(tags.values.toList())

    private fun findNextFreeTagId(): Long {
        while (Files.newDirectoryStream(Paths.get(basePath), "${nextTagId.addAndGet(1)}_*").count() > 0) { }
        return nextTagId.get()
    }

    private fun findNextFreeImageId(): Long {
        while (Files.newDirectoryStream(Paths.get(basePath, "data"), "${nextImageId.addAndGet(1)}_*").count() > 0) { }
        return nextImageId.get()
    }

    override fun createTag(tag: Tag): Observable<Tag> {
        val tag = Tag(id=findNextFreeTagId(), name=tag.name)
        File(basePath, "${tag.id}_${tag.name}").mkdirs()
        tags[tag.id] = tag
        return Observable.just(tag)
    }

    override fun createImage(imageFile: File): Observable<Image> {
        val image = Image(findNextFreeImageId(), imageFile.name)
        val file = File(basePath, "data/${image.id}_${image.name}")
        file.writeBytes(imageFile.readBytes())
        image.image = SwingFXUtils.toFXImage(ImageIO.read(file), null)
        images[image.id] = image
        return Observable.just(image)
    }

    override fun getAllImages(): Observable<List<Image>> = Observable.just(images.values.toList())

    override fun addTag(image: Image, tag: Tag): Observable<Image> =
        images[image.id]?.let {
            // Do not add the same tag twice
            if (!it.tags.contains(tag)) {
                it.tags.add(tag)
                Files.createSymbolicLink(
                    File(basePath, "${tag.id}_${tag.name}/${it.id}").toPath(),
                    File(basePath, "data/${it.id}_${it.name}").toPath())
            }
            Observable.just(it)
        } ?: throw RuntimeException("Unknown image")

    override fun removeTag(image: Image, tag: Tag): Observable<Image> {
        throw NotImplementedError("Missing")
    }

}