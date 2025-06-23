package net.testiprod.midi.client

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("MidiClient")

fun main() {
    val midiHttpClient = MidiHttpClient("http://127.0.0.1", 8080)

    val scope = CoroutineScope(Dispatchers.Default)
    logger.info("Hello from Midi Client!")
//    scope.launch {
//        when (val result = midiHttpClient.getMidiDevices()) {
//            is NetworkResult.Success -> logger.info("Devices:\n${result.data.joinToString("\n")}")
//            is NetworkResult.Error -> logger.info("Error: $result")
//        }
//    }
    val midiWebSocketClient = MidiWebSocketClient("ws://localhost", 8080)
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