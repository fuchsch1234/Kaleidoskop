package de.fuchsch.kaleidoskop.gui.viewmodels

import de.fuchsch.kaleidoskop.gui.factories.KaleidoskopServiceFactory
import de.fuchsch.kaleidoskop.gui.models.Image
import javafx.beans.property.SimpleListProperty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import tornadofx.ViewModel
import tornadofx.observable

class ImagesViewModel : ViewModel() {

    val images = SimpleListProperty<Image>(this, "tags", mutableListOf<Image>().observable())

    private val kaleidoskopService = KaleidoskopServiceFactory.buildKaleidoskopService("http://localhost:8080")

    init {
        GlobalScope.async {
            loadAllImages()
        }
    }

    private suspend fun loadAllImages() = coroutineScope {
        val allImages = kaleidoskopService.getAllImagesAsync().await()
        if (allImages.isSuccessful) {
            images.addAll(allImages.body().orEmpty())
        }
    }

}