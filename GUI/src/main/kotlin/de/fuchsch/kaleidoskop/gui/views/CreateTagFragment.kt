package de.fuchsch.kaleidoskop.gui.views

import de.fuchsch.kaleidoskop.gui.viewmodels.TagsViewModel
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.ButtonBar
import tornadofx.*

class CreateTagFragment: Fragment() {

    private val tagsViewModel: TagsViewModel by inject()

    private val tagName = SimpleStringProperty()

    override val root = form {
        fieldset("New Tag") {
            field("Tag Name") {
                textfield(tagName)
            }
        }
        buttonbar {
            button("Create", type= ButtonBar.ButtonData.APPLY) {
                action {
                    tagsViewModel.createTag(tagName.value)
                    this@CreateTagFragment.close()
                }
            }
            button("Cancel", type=ButtonBar.ButtonData.CANCEL_CLOSE) { action { this@CreateTagFragment.close() } }
        }
    }
}