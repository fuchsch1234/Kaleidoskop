package de.fuchsch.kaleidoskop.gui.models

data class ImageUpdate (
    val id: Long? = null,
    val name: String? = null,
    val tags: List<Tag>? = null
)