package de.fuchsch.kaleidoskop.gui.views

import tornadofx.View
import tornadofx.borderpane
import tornadofx.left

class MainView: View("Kaleidoskop") {

    override val root = borderpane {

        left {
            add(TagsView())
        }

    }

}