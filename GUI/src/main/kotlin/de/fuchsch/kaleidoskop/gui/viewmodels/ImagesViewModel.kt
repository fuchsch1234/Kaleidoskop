package de.fuchsch.kaleidoskop.gui.viewmodels

import com.github.thomasnield.rxkotlinfx.doOnNextFx
import com.github.thomasnield.rxkotlinfx.observeOnFx
import de.fuchsch.kaleidoskop.gui.factories.KaleidoskopServiceFactory
import de.fuchsch.kaleidoskop.gui.models.Image
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javafx.beans.property.SimpleListProperty
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tornadofx.ViewModel
import tornadofx.observable
import java.io.File
import java.lang.Exception

class ImagesViewModel : ViewModel() {

    val images = SimpleListProperty<Image>(this, "images", mutableListOf<Image>().observable())

    private val kaleidoskopService = KaleidoskopServiceFactory.buildKaleidoskopService("http://localhost:8080")

    private val uploadDisposable = CompositeDisposable()

    init {
        loadAllImages()
    }

    fun upload(files: List<File>) = files.forEach { file -> uploadDisposable.add(uploadImage(file).subscribe()) }

    private fun loadAllImages() =
        kaleidoskopService.getAllImages()
            .subscribeOn(Schedulers.io())
            .doOnNextFx { images.addAll(it) }
            .flatMap {
                Observable.fromIterable(it)
                    .flatMap { image -> loadImageData(image) }
            }
            .subscribe()

    private fun loadImageData(image: Image) =
        kaleidoskopService.getImageData(image.id)
            .subscribeOn(Schedulers.io())
            .map { image.image = javafx.scene.image.Image(it.byteStream()) }

    private fun uploadImage(file: File): Observable<Image> {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form"), file.readBytes())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return kaleidoskopService.uploadImage(body)
            .subscribeOn(Schedulers.io())
            .flatMap {
                val location = it.headers()["Location"]
                kaleidoskopService.getImage(location ?: "")
            }
            .doOnNext { it.image = javafx.scene.image.Image(file.readBytes().inputStream()) }
            .doOnNextFx { images.add(it) }
    }

}