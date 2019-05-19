package de.fuchsch.kaleidoskop.server.repositories

import de.fuchsch.kaleidoskop.server.model.TagDAO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TagRepository : JpaRepository<TagDAO, Long> {

    fun findByName(name: String): Optional<TagDAO>

}