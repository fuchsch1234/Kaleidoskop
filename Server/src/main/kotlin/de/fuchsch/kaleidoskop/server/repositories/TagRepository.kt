package de.fuchsch.kaleidoskop.server.repositories

import de.fuchsch.kaleidoskop.server.model.TagDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagRepository : JpaRepository<TagDAO, Long> {

    fun findByName(name: String): Optional<TagDAO>

}