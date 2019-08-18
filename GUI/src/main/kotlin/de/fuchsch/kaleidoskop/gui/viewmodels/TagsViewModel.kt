package de.fuchsch.kaleidoskop.gui.viewmodels

import com.github.thomasnield.rxkotlinfx.observeOnFx
import de.fuchsch.kaleidoskop.gui.factories.KaleidoskopServiceFactory
import de.fuchsch.kaleidoskop.gui.models.Tag
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javafx.beans.property.SimpleListProperty
import tornadofx.ViewModel
import tornadofx.observable
import java.lang.Exception

class TagsViewModel : ViewModel() {

    val tags = SimpleListProperty<Tag>(this, "tags", mutableListOf<Tag>().observable())

    private val kaleidoskopService = KaleidoskopServiceFactory.buildKaleidoskopService("http://localhost:8080")

    init {
        loadAllTags()
    }

    private fun loadAllTags() =
        kaleidoskopService.getAllTags()
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .subscribe { tags.addAll(it) }

    fun createTag(tagName: String) =
        kaleidoskopService.createTag(Tag(0, tagName))
            .subscribeOn(Schedulers.io())
            .flatMap {
                val location = it.headers()["Location"]
                    kaleidoskopService.getTag(location ?: "")
                }
            .observeOnFx()
            .subscribe { tags.add(it) }

}