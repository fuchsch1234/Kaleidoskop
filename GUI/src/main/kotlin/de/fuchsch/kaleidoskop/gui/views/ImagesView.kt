package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.ImagesViewModel
import javafx.scene.input.TransferMode
import tornadofx.View
import tornadofx.datagrid
import tornadofx.imageview

class ImagesView : View() {

    private val imagesViewModel: ImagesViewModel by inject()

    override val root = datagrid(imagesViewModel.images) {

        cellCache {
            imageview(it.imageProperty)
        }

        setOnDragOver { event ->
            if (event.dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY)
            }
            event.consume()
        }

        setOnDragDropped { event ->
            val files = event.dragboard.files
            if (files != null) {
                imagesViewModel.upload(files)
            }
            event.isDropCompleted = files.isNotEmpty()
            event.consume()
        }

    }
}