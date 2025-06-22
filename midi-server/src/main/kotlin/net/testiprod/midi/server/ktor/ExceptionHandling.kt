package net.testiprod.midi.server.ktor

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.util.cio.ChannelWriteException
import org.slf4j.LoggerFactory


fun Application.configureExceptionHandling() {
    val errorLogger = LoggerFactory.getLogger("net.testiprod.pianoman.server")
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is ChannelWriteException -> {
                    errorLogger.warn("ChannelWriteException: ${cause.message}")
                    call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
                }

                else -> {
                    errorLogger.error(cause.message, cause)
                    call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}
