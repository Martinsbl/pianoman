package net.testiprod.midi.client.ktor

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

private fun GsonBuilder.configureGson(): GsonBuilder {
    setPrettyPrinting()
    serializeNulls()
    return this
}

val commonGson: Gson = GsonBuilder()
    .configureGson()
    .create()

fun HttpClientConfig<CIOEngineConfig>.configureGson() {
    install(ContentNegotiation) {
        gson {
            configureGson()
        }
    }
}