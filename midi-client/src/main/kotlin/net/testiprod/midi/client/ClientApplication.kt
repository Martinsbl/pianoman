package net.testiprod.midi.client

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import net.testiprod.pianoman.transport.TMidiMessage
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("MidiClient")

fun main() {
    val midiHttpClient = MidiHttpClient("http://127.0.0.1", 8080)
    val midiWebSocketClient = MidiWebSocketClient("ws://localhost", 8080)

    val scope = CoroutineScope(Dispatchers.Default)
    logger.info("Hello from Midi Client!")
//    scope.launch {
//        when (val result = midiHttpClient.getMidiDevices()) {
//            is NetworkResult.Success -> logger.info("Devices:\n${result.data.joinToString("\n")}")
//            is NetworkResult.Error -> logger.info("Error: $result")
//        }
//    }

    logger.info("Attempting to connect to WebSocket...")
    midiWebSocketClient.connectToTransmitWebSocket(1258500461, scope)
    logger.info("WebSocket connected successfully")
    scope.launch {
        repeat(5) {
            val message = TMidiMessage(144, 108, 40)
            logger.info("Sending MIDI message: $message")
            midiWebSocketClient.sendMidiMessage(message)
            delay(1000) // Wait for a second before sending the next message
        }
    }

    scope.launch {
        withTimeout(5.seconds) {
            val flow = midiWebSocketClient.getMidiMessageFlow(873200140)
            flow.collect { message ->
                logger.info("Received MIDI message: $message")
            }
        }
    }
    Thread.sleep(10_000) // Keep the main thread alive for a while to see the output
    logger.info("Exiting Midi Client")
}