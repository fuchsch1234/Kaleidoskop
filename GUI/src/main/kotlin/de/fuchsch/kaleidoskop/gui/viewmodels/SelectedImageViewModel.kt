package de.fuchsch.kaleidoskop.gui.viewmodels

import de.fuchsch.kaleidoskop.gui.models.Image
import de.fuchsch.kaleidoskop.gui.models.Tag
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import tornadofx.ItemViewModel
import tornadofx.observable

class SelectedImageViewModel: ItemViewModel<Image>() {

    val selectedImage = SimpleObjectProperty<Image?>(this, "selectedImage", null)

    val image = Bindings.createObjectBinding(
        { selectedImage.value?.image },
        arrayOf(selectedImage))

    val tags = Bindings.createObjectBinding(
        { selectedImage.value?.tagsProperty ?: emptyList<Tag>().observable()},
        arrayOf(selectedImage))

}