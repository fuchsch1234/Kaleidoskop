package de.fuchsch.kaleidoskop.gui.viewmodels

import com.github.thomasnield.rxkotlinfx.observeOnFx
import de.fuchsch.kaleidoskop.gui.models.Repository
import de.fuchsch.kaleidoskop.gui.models.Tag
import javafx.beans.property.SimpleListProperty
import tornadofx.ViewModel
import tornadofx.observable

class TagsViewModel : ViewModel() {

    private val tagsProperty = SimpleListProperty<Tag>(this, "tags", mutableListOf<Tag>().observable())

    val tags = tagsProperty.sorted { o1, o2 -> o1.name.compareTo(o2.name) }

    private val repository: Repository by di()

    init {
        repository.getTags().observeOnFx().subscribe { tagsProperty.add(it) }
    }

    fun createTag(tagName: String) = repository.createTag(tagName)

}