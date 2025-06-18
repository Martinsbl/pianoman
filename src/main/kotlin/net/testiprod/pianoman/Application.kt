package net.testiprod.pianoman

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "localhost",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureFrameworks()
    configureRouting()
}
