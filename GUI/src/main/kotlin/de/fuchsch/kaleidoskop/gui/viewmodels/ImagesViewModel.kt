package de.fuchsch.kaleidoskop.gui.viewmodels

import de.fuchsch.kaleidoskop.gui.factories.KaleidoskopServiceFactory
import de.fuchsch.kaleidoskop.gui.models.Image
import javafx.beans.property.SimpleListProperty
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tornadofx.ViewModel
import tornadofx.observable
import java.io.File

class ImagesViewModel : ViewModel() {

    val images = SimpleListProperty<Image>(this, "images", mutableListOf<Image>().observable())

    private val kaleidoskopService = KaleidoskopServiceFactory.buildKaleidoskopService("http://localhost:8080")

    init {
        GlobalScope.launch { loadAllImages() }
    }

    fun upload(files: List<File>) {
        for (file in files) {
            GlobalScope.launch { uploadImage(file) }
        }
    }

    private suspend fun loadAllImages() = coroutineScope {
        val allImages = kaleidoskopService.getAllImagesAsync().await()
        if (allImages.isSuccessful) {
            withContext(Dispatchers.Main) { images.addAll(allImages.body().orEmpty()) }
            for (image in images) {
                launch { loadImageData(image) }
            }
        }
    }

    private suspend fun loadImageData(image: Image) = coroutineScope {
        val response = kaleidoskopService.getImageData(image.id).await()
        image.image = javafx.scene.image.Image(response.byteStream())
    }

    private suspend fun uploadImage(file: File) = coroutineScope {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form"), file.readBytes())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val response = kaleidoskopService.uploadImageAsync(body).await()
        if (response.isSuccessful) {
            val image = Image(0, "0")
            image.image = javafx.scene.image.Image(file.inputStream())
            withContext(Dispatchers.Main) { images.add(image) }
        }
    }

}