package de.fuchsch.kaleidoskop.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

@SpringBootApplication
@EnableSpringDataWebSupport
class Server {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Server>(*args)
        }

    }

}


