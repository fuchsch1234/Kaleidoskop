package de.fuchsch.kaleidoskop.server.repositories

import de.fuchsch.kaleidoskop.server.model.ImageDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<ImageDAO, Long> {

    fun findByTagsName(name: String): List<ImageDAO>

}