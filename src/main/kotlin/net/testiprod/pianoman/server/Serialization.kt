package net.testiprod.pianoman.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

private fun GsonBuilder.configureGson(): GsonBuilder {
    setPrettyPrinting()
    serializeNulls()
    return this
}

val commonGson: Gson = GsonBuilder()
    .configureGson()
    .create()

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            configureGson()
        }
    }
}
