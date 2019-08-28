package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.models.Tag
import de.fuchsch.kaleidoskop.gui.viewmodels.SelectedImageViewModel
import javafx.collections.ObservableList
import tornadofx.*

class SingleImageView: Fragment() {

    private val selectedImageViewModel: SelectedImageViewModel by inject()

    override val root = hbox {

        vbox {
            listview(selectedImageViewModel.tags as ObservableList<Tag>) {
                cellCache {
                    label(it.nameProperty)
                }
            }

            listview(selectedImageViewModel.possibleTags as ObservableList<Tag>) {
                cellCache {
                    hbox {
                        label(it.nameProperty)
                        hyperlink("+") {
                            action { selectedImageViewModel.addTag(it) }
                        }
                    }
                }
            }
        }

        imageview(selectedImageViewModel.image) {
            isPreserveRatio = true
            fitWidth = 1024.0
            fitHeight = 768.0
        }

    }

}