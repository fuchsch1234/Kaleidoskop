package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.SlideShowViewModel
import tornadofx.*

class SlideShowView: Fragment() {

    private val slideShowViewModel: SlideShowViewModel by inject()

    override val root = borderpane {
        center {
            imageview(slideShowViewModel.currentImage) {
                isPreserveRatio = true
                fitWidthProperty().bind(widthProperty())
                fitHeightProperty().bind(heightProperty())
            }
        }
    }

    override fun onBeforeShow() {
        super.onBeforeShow()
        slideShowViewModel.reset()
    }

    override fun onDock() {
        super.onDock()
        modalStage?.isMaximized = true
    }

}