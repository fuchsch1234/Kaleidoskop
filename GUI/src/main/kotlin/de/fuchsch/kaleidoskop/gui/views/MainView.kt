package de.fuchsch.kaleidoskop.gui.views

import tornadofx.View
import tornadofx.borderpane

class MainView: View("Kaleidoskop") {

    override val root = borderpane {

        left<TagsView>()

        center<ImagesView>()

    }

}