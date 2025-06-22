package net.testiprod.midi.server.midi

import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import javax.sound.midi.MidiMessage
import kotlinx.coroutines.flow.receiveAsFlow
import net.testiprod.midi.server.ktor.commonGson
import net.testiprod.pianoman.transport.TMidiMessage
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("net.testiprod.pianoman.midi.WebSocket")

fun Route.configureWebSockets() {


    webSocket("/ws-send") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Missing device ID")
        useMidiDevice(id) { device ->
            incoming.receiveAsFlow().collect { frame ->
                when (frame) {
                    is Frame.Text -> {
                        val midiEvent = frame.readText()
                        val tMidiMessage = commonGson.fromJson(midiEvent, TMidiMessage::class.java)
                        val midiMessage: MidiMessage = tMidiMessage.toMidiMessage()
                        try {
                            device.receiver.send(midiMessage, -1L)
                        } catch (e: Exception) {
                            logger.error("Error processing MIDI event: $midiEvent", e)
                        }
                        logger.info(midiEvent)
                    }

                    else -> logger.warn("Received frame $frame")
                }
            }

        }
    }
    webSocket("/ws-receive") {
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Missing device ID")
        useMidiDevice(id) { device ->
            val receiver = WebSocketMidiReceiver(this)
            device.transmitter.receiver = receiver
            incoming.receiveAsFlow().collect { frame ->
                // This block is mostly to keep the session alive until the client disconnects
                logger.debug("Received frame {}", frame)
            }
        }
    }
}
