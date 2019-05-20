package de.fuchsch.kaleidoskop.server.integrationtest.testrunner

import de.fuchsch.kaleidoskop.server.Server
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner
import org.springframework.boot.runApplication

class ZerocodeSpringBootRunner (klass: Class<*>) : ZeroCodeUnitRunner(klass) {

    init {
        runApplication<Server>()
    }

}