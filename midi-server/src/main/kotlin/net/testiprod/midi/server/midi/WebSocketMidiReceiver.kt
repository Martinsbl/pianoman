package net.testiprod.midi.server.midi

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.Frame
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.testiprod.midi.server.ktor.commonGson
import org.slf4j.LoggerFactory

class WebSocketMidiReceiver(
    private val session: DefaultWebSocketServerSession,
) : Receiver {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun send(message: MidiMessage, timeStamp: Long) {
        val transportMessage = message.toTransport()
        logger.trace("Sending MIDI message: {}", transportMessage)
        if (session.coroutineContext.isActive) {
            val json = commonGson.toJson(transportMessage)
            session.launch {
                session.send(Frame.Text(json))
            }
        } else {
            logger.warn("Session is not active, cannot send MIDI message: {}", transportMessage)
        }
    }

    override fun close() {
        logger.trace("Closing WebSocketMidiReceiver")
    }
}