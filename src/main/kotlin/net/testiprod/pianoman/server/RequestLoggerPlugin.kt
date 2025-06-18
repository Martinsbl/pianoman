package net.testiprod.pianoman.server

import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import org.slf4j.LoggerFactory

val RequestLoggerPlugin = createApplicationPlugin("RequestLoggerPlugin") {
    val logger = LoggerFactory.getLogger("net.testiprod.pianoman.server.RequestLogger")
    onCall { call ->
        logger.info("${call.request.httpMethod.value} ${call.request.uri}")
    }
}