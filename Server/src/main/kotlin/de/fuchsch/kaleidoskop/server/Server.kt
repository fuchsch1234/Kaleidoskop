package de.fuchsch.kaleidoskop.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Server {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Server>(*args)
        }

    }

}


