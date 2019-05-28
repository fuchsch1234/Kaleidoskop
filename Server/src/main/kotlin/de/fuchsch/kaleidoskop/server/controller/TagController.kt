package de.fuchsch.kaleidoskop.server.controller

import de.fuchsch.kaleidoskop.server.model.TagDAO
import de.fuchsch.kaleidoskop.server.repositories.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/tags")
class TagController (
    @Autowired val tagRepository: TagRepository
) {

    @PostMapping("", produces=["application/json"])
    fun create(@RequestBody tag: TagDAO): ResponseEntity<Unit> {
        tagRepository.save(tag)
        return created(URI("/tags/${tag.id}")).build()
    }

    @GetMapping("", produces=["application/json"])
    fun getAll() = tagRepository.findAll().map { ok(it) }

    @GetMapping("/{id:[\\d]+}", produces=["application/json"])
    fun getById(@PathVariable id: Long) = tagRepository.findById(id).map { ok(it) }.orElse(notFound().build())

}