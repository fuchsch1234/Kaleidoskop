package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Tag
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface KaleidoskopService {

    @GET("/api/v1/tags")
    fun getAllTagsAsync(): Deferred<Response<List<Tag>>>

    @POST("/api/v1/tags")
    fun createTagAsync(@Body tag: Tag): Deferred<Response<Unit>>

    @POST("/api/v1/images")
    @Multipart
    fun uploadImageAsync(@Part file: MultipartBody.Part): Deferred<Response<Unit>>

    @GET("{url}")
    fun getImageAsync(@Path(value="url", encoded=true) url: String): Deferred<Response<Image>>

    @GET("/api/v1/images")
    fun getAllImagesAsync(): Deferred<Response<List<Image>>>

    @GET("/api/v1/images/files/{id}")
    fun getImageData(@Path(value="id") id: Long): Deferred<ResponseBody>

    @GET("{url}")
    fun getTagAsync(@Path(value="url", encoded=true) url: String): Deferred<Response<Tag>>

    @POST("/api/v1/images/{id}/relationships/tags")
    fun addTagAsync(@Path(value="id") id: Long, @Body tag: Tag): Deferred<Response<Image>>

    @DELETE("/api/v1/images/{id}/relationships/tags/{tagId}")
    fun removeTagAsync(@Path(value="id") id: Long, @Path(value="tagId") tagId: Long): Deferred<Response<Image>>

}