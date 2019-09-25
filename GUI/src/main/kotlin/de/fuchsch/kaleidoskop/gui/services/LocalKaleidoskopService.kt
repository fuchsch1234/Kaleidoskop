package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Tag
import io.reactivex.Observable
import java.io.File
import java.lang.RuntimeException

class LocalKaleidoskopService(private val basePath: String): KaleidoskopService {

    private val TAG_REGEX = Regex("""(\d+)_(.*)""")

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

    override fun createTag(tag: Tag): Observable<Tag> {
        throw NotImplementedError("Missing")
    }

    override fun createImage(image: Image): Observable<Image> {
        throw NotImplementedError("Missing")
    }

    override fun getImage(url: String): Observable<Image> {
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