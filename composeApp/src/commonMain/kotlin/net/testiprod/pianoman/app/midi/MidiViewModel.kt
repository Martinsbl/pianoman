package net.testiprod.pianoman.app.midi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.logging.LogLevel
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.testiprod.midi.client.MidiHttpClient
import net.testiprod.midi.client.MidiWebSocketClient
import net.testiprod.pianoman.app.music.MidiTimings
import net.testiprod.pianoman.app.toUiState
import net.testiprod.pianoman.app.ui.UiState
import net.testiprod.pianoman.transport.TMidiDeviceInfo
import net.testiprod.pianoman.transport.TMidiMessage
import org.slf4j.LoggerFactory

class MidiViewModel : ViewModel() {

    private val logger = LoggerFactory.getLogger("App")

    private val favoriteMidiDeviceId = 1258500461
    private val midiHttpClient = MidiHttpClient("http://127.0.0.1", 8080, LogLevel.INFO)
    private val midiWebSocketClient = MidiWebSocketClient("ws://127.0.0.1", 8080)

    private val _deviceListState = MutableStateFlow<UiState<List<TMidiDeviceInfo>>>(UiState.Loading)
    val deviceListState: StateFlow<UiState<List<TMidiDeviceInfo>>> = _deviceListState.asStateFlow()

    init {
        fetchMidiDevices()
    }

    fun fetchMidiDevices() {
        _deviceListState.value = UiState.Loading
        viewModelScope.launch {
            delay(1.seconds)
            val result = midiHttpClient.getMidiDevices()
            _deviceListState.value = result.toUiState()
            connectToFavoriteIfPresent()
        }
    }

    private fun connectToFavoriteIfPresent() {
        if (deviceListState.value is UiState.Success) {
            val devicesList = (deviceListState.value as UiState.Success<List<TMidiDeviceInfo>>).data
            val favorite = devicesList.firstOrNull { it.id == favoriteMidiDeviceId }
            if (favorite != null) {
                openMidiWebSocket(favorite.id)
            }
        }
    }

    fun openMidiWebSocket(deviceId: Int) {
        logger.info("Opening MIDI WebSocket for device $deviceId")
        midiWebSocketClient.connectToTransmitWebSocket(deviceId, viewModelScope)
    }

    fun onKeyPress(key: Int) {
        logger.info("Key pressed: $key")
        viewModelScope.launch {
            val message = TMidiMessage(144, key, 40)
            midiWebSocketClient.sendMidiMessage(message)
//            midiWebSocketClient.sendMidiMessage(message.copy(note = key + 3))
//            midiWebSocketClient.sendMidiMessage(message.copy(note = key + 7))
        }
    }

    fun onKeyRelease(key: Int) {
        logger.info("Key released: $key")
        viewModelScope.launch {
            val message = TMidiMessage(128, key, 0)
            midiWebSocketClient.sendMidiMessage(message)
        }
    }

    fun playSong(song: List<MidiTimings>) {
        logger.info("Playing song with ${song.size} notes")
        viewModelScope.launch {
            for (note in song) {
                onKeyPress(note.note)
                delay(note.startTime)
//                onKeyRelease(note.note)
            }
        }
    }
}