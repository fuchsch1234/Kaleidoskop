package de.fuchsch.kaleidoskop.gui

import de.fuchsch.kaleidoskop.gui.factories.KaleidoskopServiceFactory
import de.fuchsch.kaleidoskop.gui.models.Repository
import de.fuchsch.kaleidoskop.gui.views.MainView
import javafx.stage.Stage
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import kotlin.reflect.KClass

class Kaleidoskop: App(MainView::class) {

    private val kaleidoskopModule = module {
        single { Repository(get()) }
        single { KaleidoskopServiceFactory.buildKaleidoskopService(getProperty("server_url")) }
    }

    override fun start(stage: Stage) {
        startKoin {
            fileProperties("/kaleidoskop.properties")
            environmentProperties()
            modules(kaleidoskopModule)
        }
        FX.dicontainer = object: DIContainer, KoinComponent {
            override fun <T : Any> getInstance(type: KClass<T>): T = getKoin().get(type, null, null)
        }
        super.start(stage)
        stage.isMaximized = true
    }

}