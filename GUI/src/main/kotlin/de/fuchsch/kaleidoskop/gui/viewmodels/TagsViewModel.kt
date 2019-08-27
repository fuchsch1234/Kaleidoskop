package de.fuchsch.kaleidoskop.gui.viewmodels

import com.github.thomasnield.rxkotlinfx.observeOnFx
import de.fuchsch.kaleidoskop.gui.models.Repository
import de.fuchsch.kaleidoskop.gui.models.Tag
import javafx.beans.property.SimpleListProperty
import tornadofx.ViewModel
import tornadofx.observable

class TagsViewModel : ViewModel() {

    val tags = SimpleListProperty<Tag>(this, "tags", mutableListOf<Tag>().observable())

    private val repository: Repository by di()

    init {
        repository.getTags().observeOnFx().subscribe { tags.add(it) }
    }

    fun createTag(tagName: String) = repository.createTag(tagName)

}