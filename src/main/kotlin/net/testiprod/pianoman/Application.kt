package net.testiprod.pianoman

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
import net.testiprod.pianoman.midi.configureMidiRouting
import net.testiprod.pianoman.midi.connectedDevice
import net.testiprod.pianoman.midi.getMidiDeviceInfo
import net.testiprod.pianoman.server.RequestLoggerPlugin
import net.testiprod.pianoman.server.configureExceptionHandling
import net.testiprod.pianoman.server.configureSerialization
import net.testiprod.pianoman.server.configureSwagger
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ApplicationKt")

fun main() {
    val config = AppConfig.fromFile("application.yaml")
    midiStuff()

    val devices = getMidiDeviceInfo()
    logger.info("Available MIDI devices: ${devices.joinToString { "\n${it.name} (${it.vendor})" }}")

    onShutdown()

    embeddedServer(
        Netty,
        port = config.server.port,
        host = config.server.host,
        module = Application::module
    ).start(wait = true)
}

fun onShutdown() {
    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info("Shutting down application...")
        connectedDevice?.let {
            logger.info("Disconnecting from MIDI device: ${it.deviceInfo.name}")
        }
        logger.info("Application shutdown complete.")
    })
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