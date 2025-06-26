package net.testiprod.pianoman.app.midi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.logging.LogLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.testiprod.midi.client.MidiHttpClient
import net.testiprod.pianoman.app.toUiState
import net.testiprod.pianoman.app.ui.UiState
import net.testiprod.pianoman.transport.TMidiDeviceInfo
import org.slf4j.LoggerFactory

class MidiViewModel : ViewModel() {

    private val logger = LoggerFactory.getLogger("App")

    private val midiHttpClient = MidiHttpClient("http://127.0.0.1", 8080, LogLevel.INFO)

    private val _deviceListState = MutableStateFlow<UiState<List<TMidiDeviceInfo>>>(UiState.Loading)
    val deviceListState: StateFlow<UiState<List<TMidiDeviceInfo>>> = _deviceListState.asStateFlow()

    fun fetchMidiDevices() {
        _deviceListState.value = UiState.Loading
        viewModelScope.launch {
            val result = midiHttpClient.getMidiDevices()
            _deviceListState.value = result.toUiState()
        }
    }

}