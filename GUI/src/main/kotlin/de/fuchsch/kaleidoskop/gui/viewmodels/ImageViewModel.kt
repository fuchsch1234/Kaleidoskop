package de.fuchsch.kaleidoskop.gui.viewmodels

import de.fuchsch.kaleidoskop.gui.models.Image
import tornadofx.ItemViewModel

class ImageViewModel(image: Image): ItemViewModel<Image>(image) {
    val name = bind(Image::nameProperty)
    val tags = bind(Image::tagsProperty)
}