package de.fuchsch.kaleidoskop.gui.views

import tornadofx.*

class MainView: View("Kaleidoskop") {

    override val root = borderpane {

        left<TagsView>()

        center<ImagesView>()

        bottom {
            button("Slideshow") {
                action {
                    find<SlideShowView>().openModal()
                }
            }
        }

    }

}