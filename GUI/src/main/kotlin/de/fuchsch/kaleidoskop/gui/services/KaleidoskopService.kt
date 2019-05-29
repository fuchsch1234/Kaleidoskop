package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.ImageUpdate
import de.fuchsch.kaleidoskop.gui.models.Tag
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface KaleidoskopService {

    @GET("/api/v1/tags")
    fun getAllTagsAsync(): Deferred<Response<List<Tag>>>

    @POST("/api/v1/tags")
    fun createTagAsync(@Body tag: Tag): Deferred<Response<Tag>>

    @POST("/api/v1/images")
    @Multipart
    fun uploadImageAsync(@Part file: MultipartBody.Part): Deferred<Response<Image>>

    @GET("/api/v1/images")
    fun getAllImagesAsync(): Deferred<Response<List<Image>>>

    @PATCH("/api/v1/images/{id}")
    fun updateImageAsync(@Path(value="id") id: Long, @Body update: ImageUpdate): Deferred<Response<Image>>

}