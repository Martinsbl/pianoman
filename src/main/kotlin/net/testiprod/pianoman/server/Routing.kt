package net.testiprod.pianoman.server

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import org.slf4j.LoggerFactory


fun Application.configureExceptionHandling() {
    val errorLogger = LoggerFactory.getLogger("net.testiprod.pianoman.server")
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            errorLogger.error(cause.message, cause)
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
}
