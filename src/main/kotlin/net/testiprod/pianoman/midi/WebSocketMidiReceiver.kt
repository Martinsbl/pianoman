package net.testiprod.pianoman.midi

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.Frame
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class WebSocketMidiReceiver(
    private val session: DefaultWebSocketServerSession,
) : Receiver {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun send(message: MidiMessage, timeStamp: Long) {
        val transportMessage = message.toTransport()
        logger.trace("Sending MIDI message: {}", transportMessage)
        if (session.coroutineContext.isActive) {
            session.launch {
                session.send(Frame.Text(transportMessage.toString()))
            }
        } else {
            logger.warn("Session is not active, cannot send MIDI message: {}", transportMessage)
        }
    }

    override fun close() {
        logger.trace("Closing WebSocketMidiReceiver")
    }
}