package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.SelectedImageViewModel
import de.fuchsch.kaleidoskop.gui.viewmodels.ImagesViewModel
import javafx.scene.input.TransferMode
import tornadofx.View
import tornadofx.datagrid
import tornadofx.imageview

class ImagesView : View() {

    private val imagesViewModel: ImagesViewModel by inject()

    private val selectedImageViewModel: SelectedImageViewModel by inject()

    override val root = datagrid(imagesViewModel.images) {

        cellWidth = 300.0
        cellHeight = cellWidth

        cellCache {
            imageview(it.imageProperty) {
                isPreserveRatio = true
                fitWidth = cellWidth - 20.0
                fitHeight = cellHeight - 20.0
            }
        }

        onUserSelect {
            selectedImageViewModel.selectedImage.set(it)
            find<SingleImageView>().openModal()
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