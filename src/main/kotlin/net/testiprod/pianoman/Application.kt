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

    midiStuff()

    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
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