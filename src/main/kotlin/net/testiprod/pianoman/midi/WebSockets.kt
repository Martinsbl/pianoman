package net.testiprod.pianoman.midi

import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.receiveAsFlow
import org.slf4j.LoggerFactory

fun Route.configureWebSockets() {

    val logger = LoggerFactory.getLogger("net.testiprod.pianoman.midi.WebSocket")

    webSocket("/websocket") {
        try {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Missing device ID")
            val device = getMidiDevice(id)

            val flow = midiMessagesFlow(device)
            flow.collect {
                // Send MIDI messages to the WebSocket client
                send(Frame.Text(it.toTransport().toString()))
                logger.info("Sent MIDI message: ${it.message.joinToString(", ")}")
            }

            // Handle incoming messages from a client
            incoming.receiveAsFlow().collect { frame ->
                when (frame) {
                    is Frame.Text -> {
                        val midiEvent = frame.readText()
                        // Process received MIDI event
                        logger.info(midiEvent)
                        println(midiEvent)
                    }

                    else -> logger.warn("Received frame $frame")
                }
            }
        } catch (e: Exception) {
            logger.error("Error while receiving midi event", e)
        } finally {
            // Clean up when connection closes
            logger.info("WebSocket connection closed")
        }
    }
}