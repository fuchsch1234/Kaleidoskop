package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Tag
import io.reactivex.Observable
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicLong

class LocalKaleidoskopService(private val basePath: String): KaleidoskopService {

    private val TAG_REGEX = Regex("""(\d+)_(.*)""")

    private val nextTagId = AtomicLong(0)

    override fun getAllTags(): Observable<List<Tag>> =
        Observable.just(
            File(basePath).walk()
                .filter { it.isDirectory }
                .filter { TAG_REGEX.matches(it.name) }
                .map { file ->
                    TAG_REGEX.find(file.name)?.let {
                        Tag(it.groupValues[1].toLong(), it.groupValues[2])
                    } ?: throw RuntimeException("Regex")
                }.toList()
        )

    private fun findNextFreeId(): Long {
        while (Files.newDirectoryStream(Paths.get(basePath), "${nextTagId.addAndGet(1)}_*").count() > 0) { }
        return nextTagId.get()
    }

    override fun createTag(tag: Tag): Observable<Tag> {
        val tag = Tag(id=findNextFreeId(), name=tag.name)
        File(basePath, "${tag.id}_${tag.name}").mkdirs()
        return Observable.just(tag)
    }

    override fun createImage(image: Image): Observable<Image> {
        throw NotImplementedError("Missing")
    }

    override fun getAllImages(): Observable<List<Image>> {
        throw NotImplementedError("Missing")
    }

    override fun addTag(image: Image, tag: Tag): Observable<Image> {
        throw NotImplementedError("Missing")
    }

    override fun removeTag(image: Image, tag: Tag): Observable<Image> {
        throw NotImplementedError("Missing")
    }

}