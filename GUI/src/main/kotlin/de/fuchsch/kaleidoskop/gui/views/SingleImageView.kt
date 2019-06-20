package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.SelectedImageViewModel
import tornadofx.*

class SingleImageView: Fragment() {

    private val selectedImageViewModel: SelectedImageViewModel by inject()

    override val root = hbox {

        listview(selectedImageViewModel.tags.value) {
            cellCache {
                label(it.nameProperty)
            }
        }

        imageview(selectedImageViewModel.image) {
            isPreserveRatio = true
            fitWidth = 1024.0
            fitHeight = 768.0
        }

    }

}