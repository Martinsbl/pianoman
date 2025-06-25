package net.testiprod.pianoman.app.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.testiprod.pianoman.transport.TMidiDeviceInfo

@Composable
fun MidiDeviceView(
    midiDevice: TMidiDeviceInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(midiDevice.name, style = MaterialTheme.typography.titleMedium)
            Text(midiDevice.vendor)
            Text(midiDevice.description)
            Text(midiDevice.version)
        }
    }
}


@Preview
@Composable
private fun Preview() {
    Scaffold {
        Column {
            MidiDeviceView(
                TMidiDeviceInfo(
                    id = 1791575921,
                    name = "Gervill",
                    vendor = "OpenJDK",
                    description = "Software MIDI Synthesizer",
                    version = "1.0"
                ),
                onClick = {},
            )
        }
    }
}