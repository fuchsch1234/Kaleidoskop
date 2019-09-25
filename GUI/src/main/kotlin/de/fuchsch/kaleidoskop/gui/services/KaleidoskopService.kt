package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Tag
import io.reactivex.Observable

interface KaleidoskopService {

    fun getAllTags(): Observable<List<Tag>>

    fun createTag(tag: Tag): Observable<Tag>

    fun createImage(image: Image): Observable<Image>

    fun getAllImages(): Observable<List<Image>>

    fun addTag(image: Image, tag: Tag): Observable<Image>

    fun removeTag(image: Image, tag: Tag): Observable<Image>

}