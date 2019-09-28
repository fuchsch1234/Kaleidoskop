package de.fuchsch.kaleidoskop.gui.models

import tornadofx.*
import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image

class Image(id: Long, name: String, tags: MutableList<Tag> = mutableListOf()) {
    var id = id

    @JsonIgnore
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    @JsonIgnore
    val tagsProperty = SimpleListProperty<Tag>(tags.observable())
    val tags by tagsProperty

    @JsonIgnore
    val imageProperty = SimpleObjectProperty<Image>(this, "image")
    var image by imageProperty
}