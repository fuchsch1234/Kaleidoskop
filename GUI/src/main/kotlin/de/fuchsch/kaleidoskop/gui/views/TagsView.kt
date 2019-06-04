package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.TagsViewModel
import tornadofx.*

class TagsView : View() {

    val tagsViewModel: TagsViewModel by inject()

    override val root = vbox {

        listview(tagsViewModel.tags) {
            cellCache {
                hbox {
                    label(it.nameProperty)
                    button("+")
                }
            }
        }

    }

}