package net.testiprod.pianoman.server

import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.pianoman.server.HTTP")

fun Application.configureHTTP() {
    routing {
        swaggerUI(path = "openapi")
        logger.info("Swagger docs at http://127.0.0.1:8080/openapi")
    }
}
