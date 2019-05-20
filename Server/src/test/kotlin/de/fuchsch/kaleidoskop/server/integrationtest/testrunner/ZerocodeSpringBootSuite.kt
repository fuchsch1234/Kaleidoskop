package de.fuchsch.kaleidoskop.server.integrationtest.testrunner

import de.fuchsch.kaleidoskop.server.Server
import org.jsmart.zerocode.core.runner.ZeroCodePackageRunner
import org.springframework.boot.runApplication

class ZerocodeSpringBootSuite (klass: Class<*>) : ZeroCodePackageRunner(klass) {

    companion object {

        init {
            runApplication<Server>()
        }
    }
}