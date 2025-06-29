package net.testiprod.pianoman.app.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import net.testiprod.pianoman.app.ai.AiViewModel
import net.testiprod.pianoman.app.midi.MidiViewModel
import net.testiprod.pianoman.app.ui.MidiDeviceListView
import net.testiprod.pianoman.app.ui.PianoView
import net.testiprod.pianoman.app.ui.UiState
import net.testiprod.pianoman.app.ui.mock.mockMidiDeviceInfo
import net.testiprod.pianoman.transport.TMidiDeviceInfo

@Composable
fun MainScreen(
    viewModel: MidiViewModel,
    aiViewModel: AiViewModel,
) {
    val midiDevicesState by viewModel.deviceListState.collectAsState()
    Scaffold { paddingValues ->
        MainScreenContent(
            midiDevices = midiDevicesState,
            onFetchDevicesClick = viewModel::fetchMidiDevices,
            onDeviceClick = viewModel::openMidiWebSocket,
            onKeyPress = viewModel::onKeyPress,
            onKeyRelease = viewModel::onKeyRelease,
            onButtonClick = {
                aiViewModel.chat("Play Moonlight Sonata")
//                viewModel.playSong(moonlightSonataNotes)
            },
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun MainScreenContent(
    midiDevices: UiState<List<TMidiDeviceInfo>>,
    onFetchDevicesClick: () -> Unit,
    onDeviceClick: (deviceId: Int) -> Unit,
    onKeyPress: (Int) -> Unit,
    onKeyRelease: (Int) -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        MidiDeviceListView(midiDevices, onDeviceClick, onFetchDevicesClick)
        PianoView(
            onKeyPress = onKeyPress,
            onKeyRelease = onKeyRelease,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        )
        Button(onClick = onButtonClick) {
            Text("Moonlight Sonata")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MainScreenContent(
        midiDevices = UiState.Success(mockMidiDeviceInfo),
        onFetchDevicesClick = {},
        onDeviceClick = {},
        onKeyPress = { _ -> },
        onKeyRelease = { _ -> },
        onButtonClick = {},
    )
}