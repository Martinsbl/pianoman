package net.testiprod.pianoman.app.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.testiprod.pianoman.app.midi.MidiViewModel
import net.testiprod.pianoman.app.ui.MidiDeviceView
import net.testiprod.pianoman.app.ui.PianoView
import net.testiprod.pianoman.app.ui.UiState
import net.testiprod.pianoman.app.ui.UiStateView
import net.testiprod.pianoman.app.ui.mock.mockMidiDeviceInfo
import net.testiprod.pianoman.transport.TMidiDeviceInfo

@Composable
fun MainScreen(
    viewModel: MidiViewModel = viewModel(),
) {
    val midiDevicesState by viewModel.deviceListState.collectAsState()
    Scaffold { paddingValues ->
        MainScreenContent(
            midiDevices = midiDevicesState,
            onFetchDevicesClick = viewModel::fetchMidiDevices,
            onDeviceClick = viewModel::openMidiWebSocket,
            onKeyPress = viewModel::onKeyPress,
            onKeyRelease = viewModel::onKeyRelease,
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
    }
}

@Composable
private fun MidiDeviceListView(
    midiDevices: UiState<List<TMidiDeviceInfo>>,
    onDeviceClick: (deviceId: Int) -> Unit,
    onFetchDevicesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(200.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UiStateView(
            midiDevices,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
        ) { deviceInfoList ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val sortedList = deviceInfoList.sortedBy { it.name }
                items(sortedList, key = { it.id }) { midiDevice ->
                    MidiDeviceView(
                        midiDevice = midiDevice,
                        onClick = {
                            onDeviceClick(midiDevice.id)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        Button(onClick = onFetchDevicesClick) {
            Text("Update MIDI Devices")
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
    )
}