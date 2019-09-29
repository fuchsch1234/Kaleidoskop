package de.fuchsch.kaleidoskop.gui.viewmodels

import com.github.thomasnield.rxkotlinfx.changes
import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.subscribeOnFx
import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Repository
import de.fuchsch.kaleidoskop.gui.models.Tag
import javafx.beans.property.SimpleListProperty
import tornadofx.ViewModel
import tornadofx.observable
import java.io.File

class ImagesViewModel : ViewModel() {

    val images = SimpleListProperty<Image>(this, "images", mutableListOf<Image>().observable())

    val filters = SimpleListProperty<Tag>(this, "filters", mutableListOf<Tag>(Tag(0, "")).observable())

    private val repository: Repository by di()

    init {

        filters.changes()
            .switchMap {
                images.clear()
                repository.filteredImages(filters.toSet())
            }
            .observeOnFx()
            .subscribe { images.add(it) }
        filters.clear()

    }

    fun upload(files: List<File>) =
        files.forEach { file -> repository.uploadImage(file).observeOnFx().subscribe() { images.add(it) } }

}