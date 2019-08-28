package de.fuchsch.kaleidoskop.gui.models

import com.github.thomasnield.rxkotlinfx.observeOnFx
import de.fuchsch.kaleidoskop.gui.services.KaleidoskopService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class Repository(
    private val kaleidoskopService: KaleidoskopService
) {

    private val images = mutableListOf<Image>()

    private val tagsList = mutableListOf<Tag>()

    private val tagSubject = PublishSubject.create<Tag>()

    private val imageLoaderSubject = PublishSubject.create<Image>()

    init {
        kaleidoskopService.getAllTags()
            .subscribeOn(Schedulers.io())
            .doOnNext { tags -> tags.forEach { tagSubject.onNext(it) }}
            .subscribe { tagsList.addAll(it) }

        imageLoaderSubject
            .subscribeOn(Schedulers.io())
            .subscribe { loadImageData(it) }
    }

    fun filteredImages(): Observable<Image> =
        kaleidoskopService.getAllImages()
            .subscribeOn(Schedulers.io())
            .concatMapIterable { it }
            .map { image ->
                val imageFromList = images.find { it.id == image.id }
                if (imageFromList == null) {
                    images.add(image)
                    imageLoaderSubject.onNext(image)
                    image
                } else {
                    imageFromList
                }
            }

    private fun loadImageData(image: Image) =
        kaleidoskopService.getImageData(image.id)
            .subscribeOn(Schedulers.io())
            .subscribe { image.image = javafx.scene.image.Image(it.byteStream()) }

    fun getTags(): Observable<Tag> = tagSubject.startWith(tagsList)

    fun createTag(tagName: String) {
        kaleidoskopService.createTag(Tag(0, tagName))
            .subscribeOn(Schedulers.io())
            .flatMap {
                val location = it.headers()["Location"]
                kaleidoskopService.getTag(location ?: "")
            }
            .observeOnFx()
            .subscribe { tag ->
                tagSubject.onNext(tag)
                tagsList.add(tag)
            }
    }

    fun uploadImage(file: File): Observable<Image> {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form"), file.readBytes())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return kaleidoskopService.uploadImage(body)
            .subscribeOn(Schedulers.io())
            .flatMap {
                val location = it.headers()["Location"]
                kaleidoskopService.getImage(location ?: "")
            }
            .doOnNext {
                it.image = javafx.scene.image.Image(file.readBytes().inputStream())
                images.add(it)
            }
    }

    fun addTag(image: Image, tag: Tag) {
        kaleidoskopService.addTag(image.id, tag)
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .subscribe { image.tags.add(tag) }
    }

}