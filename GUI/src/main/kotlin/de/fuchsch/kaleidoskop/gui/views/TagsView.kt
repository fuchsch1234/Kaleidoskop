package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.TagsViewModel
import javafx.stage.StageStyle
import tornadofx.*

class TagsView : View() {

    val tagsViewModel: TagsViewModel by inject()

    override val root = vbox {

        button("New Tag") {
            action { find<CreateTagFragment>().openModal(StageStyle.UTILITY) }
        }

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