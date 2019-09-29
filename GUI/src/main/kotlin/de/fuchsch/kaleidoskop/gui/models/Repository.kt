package de.fuchsch.kaleidoskop.gui.models

import com.github.thomasnield.rxkotlinfx.observeOnFx
import de.fuchsch.kaleidoskop.gui.services.KaleidoskopService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.File

class Repository(
    private val kaleidoskopService: KaleidoskopService
) {

    private val images = mutableListOf<Image>()

    private val tagsList = mutableListOf<Tag>()

    private val tagSubject = PublishSubject.create<Tag>()

    init {
        kaleidoskopService.getAllTags()
            .subscribeOn(Schedulers.io())
            .doOnNext { tags -> tags.forEach { tagSubject.onNext(it) }}
            .subscribe { tagsList.addAll(it) }
    }

    fun filteredImages(): Observable<Image> =
        kaleidoskopService.getAllImages()
            .subscribeOn(Schedulers.io())
            .concatMapIterable { it }
            .observeOnFx()
            .map { image ->
                val imageFromList = images.find { it.id == image.id }
                if (imageFromList == null) {
                    images.add(image)
                    image
                } else {
                    imageFromList
                }
            }

    fun getTags(): Observable<Tag> = tagSubject.startWith(tagsList)

    fun createTag(tagName: String) {
        kaleidoskopService.createTag(Tag(0, tagName))
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .subscribe { tag ->
                tagSubject.onNext(tag)
                tagsList.add(tag)
            }
    }

    fun uploadImage(file: File): Observable<Image> {
        return kaleidoskopService.createImage(file)
            .subscribeOn(Schedulers.io())
            .doOnNext {
                images.add(it)
            }
    }

    fun addTag(image: Image, tag: Tag) {
        kaleidoskopService.addTag(image, tag)
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .subscribe { image.tags.add(tag) }
    }

}