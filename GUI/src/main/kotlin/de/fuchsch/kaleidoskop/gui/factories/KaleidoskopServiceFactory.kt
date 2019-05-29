package de.fuchsch.kaleidoskop.gui.factories

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import de.fuchsch.kaleidoskop.gui.services.KaleidoskopService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object KaleidoskopServiceFactory {

    fun buildKaleidoskopService(baseUrl: String): KaleidoskopService {
        val jsonMapper = jacksonObjectMapper()
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(jsonMapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(baseUrl)
            .build().create(KaleidoskopService::class.java)
    }

}