package de.fuchsch.kaleidoskop.server.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

/**
 * Data access object for images.
 *
 * Every image stores the binary data, metadata for this image and all associated tags.
 *
 * @property id Primary key for this object.
 * @property name The filename of this image.
 * @property tags All tags associated with this image.
 * @property data Binary image.
 */
@Entity
data class ImageDAO (
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long,

    @Column(unique=true, nullable=false)
    val name: String,

    @Column(nullable=false)
    val mimeType: String,

    // Cascade type for this property creates and updates tags
    @ManyToMany(cascade=[CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name="image_tag",
        joinColumns=[JoinColumn(name="image_id", referencedColumnName="id")],
        inverseJoinColumns=[JoinColumn(name="tag_id", referencedColumnName="id")]
    )
    @JsonIgnoreProperties(value=["images"])
    val tags: List<TagDAO>,

    @Type(type="org.hibernate.type.BinaryType")
    @Column(nullable=false)
    @JsonIgnore
    val data: ByteArray
) {

    override fun hashCode() = Objects.hashCode(name)

    override fun equals(other: Any?) = when {
        this === other -> true
        other is ImageDAO -> this.name == other.name
        else -> false
    }

    fun updateWith(update: ImageUpdateDTO): ImageDAO = ImageDAO(
        id = update.id ?: this.id,
        name = update.name ?: this.name,
        mimeType = update.mimeType ?: this.mimeType,
        tags = update.tags ?: this.tags,
        data = this.data
        )

}