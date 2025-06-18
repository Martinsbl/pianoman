package net.testiprod.pianoman

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import javax.sound.midi.MidiSystem
import net.testiprod.pianoman.server.configureFrameworks
import net.testiprod.pianoman.server.configureHTTP
import net.testiprod.pianoman.server.configureRouting
import net.testiprod.pianoman.server.configureSerialization
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ApplicationKt")

fun main() {
    val config = AppConfig.fromFile("application.yaml")
    midiStuff()

    embeddedServer(
        Netty,
        port = config.server.port,
        host = config.server.host,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureFrameworks()
    configureRouting()
}


fun midiStuff() {
    val info = MidiSystem.getMidiDeviceInfo()
    logger.info("MIDI: info=${info.joinToString()}")
}