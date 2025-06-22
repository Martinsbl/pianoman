package net.testiprod.midi.server

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.sse.SSE
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import javax.sound.midi.MidiSystem
import kotlin.time.Duration.Companion.seconds
import net.testiprod.midi.server.midi.configureMidiRouting
import net.testiprod.midi.server.midi.getMidiDeviceInfo
import net.testiprod.midi.server.ktor.RequestLoggerPlugin
import net.testiprod.midi.server.ktor.configureExceptionHandling
import net.testiprod.midi.server.ktor.configureSerialization
import net.testiprod.midi.server.ktor.configureSwagger
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ApplicationKt")

fun main() {
    val config = AppConfig.fromFile("application.yaml")
    midiStuff()

    val devices = getMidiDeviceInfo()
    logger.info("Available MIDI devices: ${devices.joinToString { "\n${it.name} (${it.vendor})" }}")

    embeddedServer(
        Netty,
        port = config.server.port,
        host = config.server.host,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSwagger()
    configureSerialization()
    configureExceptionHandling()

    install(RequestLoggerPlugin)
    install(SSE)

    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        configureMidiRouting()
    }
}


fun midiStuff() {
    val info = MidiSystem.getMidiDeviceInfo()
    logger.info("MIDI: info=${info.joinToString()}")
}