package de.fuchsch.kaleidoskop.gui.factories

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.fuchsch.kaleidoskop.gui.services.KaleidoskopService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

object KaleidoskopServiceFactory {

    fun buildKaleidoskopService(baseUrl: String): KaleidoskopService {
        val jsonMapper = jacksonObjectMapper()
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(jsonMapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build().create(KaleidoskopService::class.java)
    }

}