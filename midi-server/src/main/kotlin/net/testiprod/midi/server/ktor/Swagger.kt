package net.testiprod.midi.server.ktor

import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.pianoman.server")

fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "openapi")
        logger.info("Swagger docs at http://127.0.0.1:8080/openapi")
    }
}
