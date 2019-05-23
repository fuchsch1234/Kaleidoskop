package de.fuchsch.kaleidoskop.server.controller

import de.fuchsch.kaleidoskop.server.model.Filter
import de.fuchsch.kaleidoskop.server.model.ImageDAO
import de.fuchsch.kaleidoskop.server.repositories.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.util.*
import javax.activation.MimetypesFileTypeMap

@RestController
@RequestMapping("/images")
class ImageController (
    @Autowired val imageRepository: ImageRepository
) {

    @GetMapping("", produces=["application/json"])
    fun getAll() = imageRepository.findAll().map { ok(it) }

    @GetMapping("/{id:[\\d]+}", produces=["application/json"])
    fun getById(@PathVariable("id") image: Optional<ImageDAO>) = image.map { ok(it) }.orElse(notFound().build())

    @PostMapping("")
    fun upload(@RequestParam("file") file: MultipartFile): ResponseEntity<Unit> {
        val name = file.originalFilename ?: UUID.randomUUID().toString()
        val mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(name)
        val image = ImageDAO(0, name, mimeType, emptyList(), file.bytes)
        return try {
            imageRepository.save(image)
            created(URI("/images/${image.id}")).build()
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @GetMapping("files/{id:[\\d]+}")
    fun download(@PathVariable("id") image: Optional<ImageDAO>) =
        image.map { ok().header(HttpHeaders.CONTENT_TYPE, it.mimeType).body(it.data) }.orElse(notFound().build())

    @PutMapping("/{id:[\\d]+}", produces=["application/json"])
    fun update(@PathVariable("id") image: Optional<ImageDAO>, @RequestBody newImage: ImageDAO) =
            image.map {
                val newEntity = newImage.copy()
                imageRepository.save(newEntity)
                ok(newEntity)
            }.orElse(notFound().build())

    @DeleteMapping("/{id:[\\d]+}")
    fun delete(@PathVariable("id") image: Optional<ImageDAO>): ResponseEntity<Unit> =
        image.map {
            imageRepository.delete(it)
            ok().build<Unit>()
        }.orElse(notFound().build())

    @PostMapping("/search/filterByTags", produces=["application/json"])
    fun filterByTags(@RequestBody filter: Filter) =
            filter.includes.map {
                imageRepository.findByTagsName(it).toSet()
            }.reduce { acc, set -> acc.intersect(set) }
}