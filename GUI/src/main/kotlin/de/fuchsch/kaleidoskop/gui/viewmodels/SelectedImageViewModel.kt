package de.fuchsch.kaleidoskop.gui.viewmodels

import de.fuchsch.kaleidoskop.gui.ext.subtract
import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Repository
import de.fuchsch.kaleidoskop.gui.models.Tag
import javafx.beans.binding.Bindings
import javafx.beans.binding.ListBinding
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.observable

class SelectedImageViewModel: ItemViewModel<Image>() {

    private val repository: Repository by di()

    val selectedImage = SimpleObjectProperty<Image?>(this, "selectedImage", null)

    val image: ObjectBinding<javafx.scene.image.Image> = Bindings.createObjectBinding(
        { selectedImage.value?.image },
        arrayOf(selectedImage))

    val tags = object: ListBinding<Tag>() {
        init { bind(selectedImage) }

        override fun computeValue(): ObservableList<Tag> =
            selectedImage.value?.tagsProperty ?: emptyList<Tag>().observable()
    }

    private val tagsViewModel: TagsViewModel by inject()

    val possibleTags = tagsViewModel.tags.subtract(tags).sorted { o1, o2 -> o1.name.compareTo(o2.name) }

    fun addTag(tag: Tag) = selectedImage.get()?.let { image -> repository.addTag(image, tag) }

}