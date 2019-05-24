package de.fuchsch.kaleidoskop.server.model

data class ImageUpdateDTO (
    val id: Long?,
    val name: String?,
    val mimeType: String?,
    val tags: List<TagDAO>?
)