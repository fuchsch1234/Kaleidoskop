package de.fuchsch.kaleidoskop.gui.services

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Tag
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface KaleidoskopService {

    @GET("/api/v1/tags")
    fun getAllTags(): Observable<List<Tag>>

    @POST("/api/v1/tags")
    fun createTag(@Body tag: Tag): Observable<Response<Unit>>

    @POST("/api/v1/images")
    @Multipart
    fun uploadImage(@Part file: MultipartBody.Part): Observable<Response<Unit>>

    @GET("{url}")
    fun getImage(@Path(value="url", encoded=true) url: String): Observable<Image>

    @GET("/api/v1/images")
    fun getAllImages(): Observable<List<Image>>

    @GET("/api/v1/images/files/{id}")
    fun getImageData(@Path(value="id") id: Long): Observable<ResponseBody>

    @GET("{url}")
    fun getTag(@Path(value="url", encoded=true) url: String): Observable<Tag>

    @POST("/api/v1/images/{id}/relationships/tags")
    fun addTag(@Path(value="id") id: Long, @Body tag: Tag): Observable<Image>

    @DELETE("/api/v1/images/{id}/relationships/tags/{tagId}")
    fun removeTag(@Path(value="id") id: Long, @Path(value="tagId") tagId: Long): Observable<Image>

}