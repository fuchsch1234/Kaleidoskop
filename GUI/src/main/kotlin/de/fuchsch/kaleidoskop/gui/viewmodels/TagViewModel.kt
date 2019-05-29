package de.fuchsch.kaleidoskop.gui.viewmodels

import de.fuchsch.kaleidoskop.gui.models.Tag
import tornadofx.ItemViewModel

class TagViewModel(tag: Tag) : ItemViewModel<Tag>(tag) {
    val name = bind(Tag::nameProperty)
    val images = bind(Tag::imagesProperty)
}