package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.ImagesViewModel
import de.fuchsch.kaleidoskop.gui.viewmodels.TagsViewModel
import javafx.stage.StageStyle
import tornadofx.*

class TagsView : View() {

    private val tagsViewModel: TagsViewModel by inject()

    private val imagesViewModel: ImagesViewModel by inject()

    override val root = vbox {

        button("New Tag") {
            action { find<CreateTagFragment>().openModal(StageStyle.UTILITY) }
        }

        listview(imagesViewModel.filters) {
            cellCache {
                hbox {
                    label(it.nameProperty)
                    hyperlink("-").action {
                        imagesViewModel.filters.remove(it)
                    }
                }
            }
        }

        listview(tagsViewModel.tags) {
            cellCache {
                hbox {
                    label(it.nameProperty)
                    hyperlink("+").action {
                        imagesViewModel.filters.add(it)
                    }
                }
            }
        }

    }

}