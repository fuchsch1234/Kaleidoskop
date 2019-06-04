package de.fuchsch.kaleidoskop.gui.viewmodels

import de.fuchsch.kaleidoskop.gui.factories.KaleidoskopServiceFactory
import de.fuchsch.kaleidoskop.gui.models.Tag
import javafx.beans.property.SimpleListProperty
import kotlinx.coroutines.*
import tornadofx.ViewModel
import tornadofx.observable

class TagsViewModel : ViewModel() {

    val tags = SimpleListProperty<Tag>(this, "tags", mutableListOf<Tag>().observable())

    private val kaleidoskopService = KaleidoskopServiceFactory.buildKaleidoskopService("http://localhost:8080")

    init {
        GlobalScope.launch(Dispatchers.IO) {
            launch { loadAllTags() }
        }
    }

    private suspend fun loadAllTags() = coroutineScope {
            val call = kaleidoskopService.getAllTagsAsync()
            val allTags = call.await()
            println("Received tags")
            if (allTags.isSuccessful) {
                println("Adding tags")
                withContext(Dispatchers.Main) { tags.addAll(allTags.body().orEmpty()) }
            } else {
                println("Error getting tags ${allTags.errorBody()}")
            }
    }

}