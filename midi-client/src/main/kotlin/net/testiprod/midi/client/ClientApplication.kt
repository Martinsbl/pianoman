package net.testiprod.midi.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("MidiClient")

fun main() {
    val midiClient = MidiClient("http://127.0.0.1", 8080)

    val scope = CoroutineScope(Dispatchers.Default)
    logger.info("Hello from Midi Client!")
    scope.launch {
        when (val result = midiClient.getMidiDevices()) {
            is NetworkResult.Success -> logger.info("Devices:\n${result.data.joinToString("\n")}")
            is NetworkResult.Error -> logger.info("Error: $result")
        }
    }
    Thread.sleep(2000) // Keep the main thread alive for a while to see the output
}