package de.fuchsch.kaleidoskop.gui

import de.fuchsch.kaleidoskop.gui.views.MainView
import javafx.stage.Stage
import tornadofx.App

class Kaleidoskop: App(MainView::class) {

    override fun start(stage: Stage) {
        super.start(stage)
        stage.isMaximized = true
    }

}