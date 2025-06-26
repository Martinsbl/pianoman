package net.testiprod.pianoman.app.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import net.testiprod.pianoman.app.ui.UiState
import net.testiprod.pianoman.app.ui.UiStateView
import net.testiprod.pianoman.transport.TMidiDeviceInfo

@Composable
fun MainScreen(
    viewModel: MidiViewModel = viewModel(),
) {
    val midiDevicesState by viewModel.deviceListState.collectAsState()
    Scaffold { paddingValues ->
        MainScreenContent(
            midiDevices = midiDevicesState,
            onFetchDevicesClicked = viewModel::fetchMidiDevices,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun MainScreenContent(
    midiDevices: UiState<List<TMidiDeviceInfo>>,
    onFetchDevicesClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = onFetchDevicesClicked) {
            Text("Update MIDI Devices")
        }
        UiStateView(midiDevices) {
            LazyColumn(
                modifier = Modifier.width(200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(it, key = { it.id }) { midiDevice ->
                    MidiDeviceView(
                        midiDevice = midiDevice,
                        onClick = { println(midiDevice) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MainScreenContent(
        midiDevices = UiState.Success(
            listOf(
                TMidiDeviceInfo(id = 1791575921, name = "Gervill", vendor = "OpenJDK", description = "Software MIDI Synthesizer", version = "1.0"),
                TMidiDeviceInfo(
                    id = 1219393805,
                    name = "Real Time Sequencer",
                    vendor = "Oracle Corporation",
                    description = "Software sequencer",
                    version = "Version 1.0"
                ),
                TMidiDeviceInfo(
                    id = 1110839041,
                    name = "Microsoft MIDI Mapper",
                    vendor = "Unknown vendor",
                    description = "Windows MIDI_MAPPER",
                    version = "5.0"
                ),
                TMidiDeviceInfo(
                    id = 85360850,
                    name = "Microsoft GS Wavetable Synth",
                    vendor = "Unknown vendor",
                    description = "Internal software synthesizer",
                    version = "1.0"
                ),
            )
        ),
        onFetchDevicesClicked = {},
    )
}