package de.fuchsch.kaleidoskop.gui.models

import tornadofx.*
import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty

class Tag(id: Long, name: String, images: List<Image> = emptyList()) {
    val id = id

    @JsonIgnore
    val nameProperty = SimpleStringProperty(this, "name", name)
    val name by nameProperty

    @JsonIgnore
    val imagesProperty = SimpleListProperty<Image>(images.observable())
    val images by imagesProperty
}