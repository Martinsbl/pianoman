package net.testiprod.midi.server.midi

import io.ktor.server.sse.ServerSSESession
import io.ktor.sse.ServerSentEvent
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class HttpMidiReceiver(
    private val session: ServerSSESession,
) : Receiver {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun send(message: MidiMessage, timeStamp: Long) {
        val transportMessage = message.toTransport()
        logger.trace("Sending MIDI message: {}", transportMessage)
        if (session.coroutineContext.isActive) {
            session.launch(Dispatchers.IO) {
                session.send(ServerSentEvent(transportMessage.toString()))
            }
        } else {
            logger.warn("Session is not active, cannot send MIDI message: {}", transportMessage)
        }
    }

    override fun close() {
        logger.trace("Closing HttpMidiReceiver")
    }
}