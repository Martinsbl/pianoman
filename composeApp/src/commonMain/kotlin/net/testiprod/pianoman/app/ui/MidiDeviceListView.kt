package net.testiprod.pianoman.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.testiprod.pianoman.transport.TMidiDeviceInfo

@Composable
fun MidiDeviceListView(
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