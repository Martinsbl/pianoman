package net.testiprod.pianoman.app.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.testiprod.pianoman.app.ai.AiViewModel
import net.testiprod.pianoman.app.midi.MidiViewModel
import net.testiprod.pianoman.app.music.moonlightSonataNotes
import net.testiprod.pianoman.app.ui.AiView
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
    val aiResponseState by aiViewModel.aiResponse.collectAsState()
    Scaffold { paddingValues ->
        MainScreenContent(
            aiResponse = aiResponseState,
            midiDevices = midiDevicesState,
            onFetchDevicesClick = viewModel::fetchMidiDevices,
            onDeviceClick = viewModel::openMidiWebSocket,
            onKeyPress = viewModel::onKeyPress,
            onKeyRelease = viewModel::onKeyRelease,
            onPromptClick = aiViewModel::chat,
            onButtonClick = {
                viewModel.playSong(moonlightSonataNotes)
            },
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun MainScreenContent(
    aiResponse: UiState<String>,
    midiDevices: UiState<List<TMidiDeviceInfo>>,
    onFetchDevicesClick: () -> Unit,
    onDeviceClick: (deviceId: Int) -> Unit,
    onKeyPress: (Int) -> Unit,
    onKeyRelease: (Int) -> Unit,
    onPromptClick: (String) -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            modifier = modifier
                .weight(1f)
        ) {
            MidiDeviceListView(
                midiDevices,
                onDeviceClick,
                onFetchDevicesClick,
                modifier = Modifier
                    .width(240.dp)
                    .height(600.dp)
                    .padding(8.dp),
            )
            PianoView(
                onKeyPress = onKeyPress,
                onKeyRelease = onKeyRelease,
                modifier = Modifier
                    .height(400.dp)
                    .width(600.dp),
            )
            AiView(
                aiResponse,
                onPromptClick,
                modifier = Modifier
                    .width(240.dp)
                    .padding(8.dp),
            )
        }
        Button(onClick = onButtonClick) {
            Text("Test")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MainScreenContent(
        midiDevices = UiState.Success(mockMidiDeviceInfo),
        aiResponse = UiState.Success("AI Response Preview"),
        onFetchDevicesClick = {},
        onDeviceClick = {},
        onKeyPress = { _ -> },
        onKeyRelease = { _ -> },
        onPromptClick = {},
        onButtonClick = {},
    )
}