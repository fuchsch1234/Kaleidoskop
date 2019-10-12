package de.fuchsch.kaleidoskop.gui.viewmodels

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import tornadofx.ViewModel
import java.util.concurrent.TimeUnit

class SlideShowViewModel: ViewModel() {

    val currentImage = SimpleObjectProperty<Image?>(this, "currentImage", null)

    private var currentIndex = 0

    private var intervalDisposable: Disposable? = null

    private val imagesViewModel: ImagesViewModel by inject()

    private fun start() {
        currentImage.value = imagesViewModel.images[currentIndex].image
        intervalDisposable = Observable.interval(5, TimeUnit.SECONDS)
            .subscribe {
                currentIndex = if (currentIndex == imagesViewModel.images.lastIndex) {
                    0
                } else {
                    currentIndex + 1
                }
                currentImage.value = imagesViewModel.images[currentIndex].image
            }
    }

    fun reset() {
        intervalDisposable?.dispose()
        currentIndex = 0
        start()
    }

}