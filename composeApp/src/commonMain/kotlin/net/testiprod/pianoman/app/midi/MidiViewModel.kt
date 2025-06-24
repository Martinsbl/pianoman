package net.testiprod.pianoman.app.midi

import io.ktor.client.plugins.logging.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.testiprod.midi.client.MidiHttpClient
import net.testiprod.midi.client.NetworkResult
import org.slf4j.LoggerFactory

class MidiViewModel(
    private val viewModelScope: CoroutineScope,
) {

    private val logger = LoggerFactory.getLogger("App")

    private val midiHttpClient = MidiHttpClient("http://127.0.0.1", 8080, LogLevel.INFO)

    private val _deviceListState = MutableStateFlow<List<String>>(emptyList())
    val deviceListState: StateFlow<List<String>> = _deviceListState.asStateFlow()


    fun fetchMidiDevices() {
        viewModelScope.launch {
            when (val result = midiHttpClient.getMidiDevices()) {
                is NetworkResult.Success -> {
                    logger.info("Devices:\n${result.data.joinToString("\n")}")
                    _deviceListState.value = result.data.map { it.name }
                }

                is NetworkResult.Error -> {
                    logger.error("Error fetching MIDI devices: $result")
                }
            }
        }
    }

}