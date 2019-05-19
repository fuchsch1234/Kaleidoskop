package de.fuchsch.kaleidoskop.server.model

import java.util.*
import javax.persistence.*

/**
 * Data access object for tags.
 *
 * A [ImageDAO] can be associated with multiple tags. Every tags has a name and stores a list of all images that
 * are associated with this tag.
 *
 * @property id Primary key.
 * @property name This tags name.
 * @property images All [ImageDAO]s that have this tag.
 */
@Entity
data class TagDAO (
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long,

    @Column(unique=true, nullable=false)
    val name: String,

    @ManyToMany(mappedBy="tags")
    val images: List<ImageDAO>
) {

    override fun hashCode() = Objects.hashCode(name)

    override fun equals(other: Any?) = when {
        this === other -> true
        other is TagDAO -> this.name == other.name
        else -> false
    }

}