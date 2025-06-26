package net.testiprod.pianoman.app.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import net.testiprod.pianoman.app.music.midiNumberToNote

@Composable
fun PianoView(
    onKeyPress: (Int) -> Unit,
    onKeyRelease: (Int) -> Unit,
    modifier: Modifier = Modifier,
    keys: List<Int> = (60..72).toList(), // Default to 88 keys
) {
    // One octave starting from middle C (MIDI note 60)
    val whiteKeys = listOf(60, 62, 64, 65, 67, 69, 71)
    val blackKeys = listOf(61, 63, 66, 68, 70)

    Box(modifier = modifier.height(200.dp)) {
        // White keys
        Row(modifier = Modifier.fillMaxSize()) {
            whiteKeys.forEach { note ->
                PianoKeyView(
                    note = note,
                    isBlack = false,
                    onKeyPress = onKeyPress,
                    onKeyRelease = onKeyRelease,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }

        // Black keys
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Position black keys with proper spacing
            blackKeys.forEachIndexed { index, note ->
                if (index == 2) {
                    Spacer(modifier = Modifier.width(40.dp))
                }
                PianoKeyView(
                    note = note,
                    isBlack = true,
                    onKeyPress = onKeyPress,
                    onKeyRelease = onKeyRelease,
                    modifier = Modifier
                        .width(40.dp)
                        .fillMaxHeight(0.6f)
                )
            }
        }
    }
}

@Composable
private fun PianoKeyView(
    note: Int,
    isBlack: Boolean,
    onKeyPress: (Int) -> Unit,
    onKeyRelease: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val backgroundColor = when {
        isPressed && isBlack -> Color.DarkGray
        isPressed && !isBlack -> Color.LightGray
        isBlack -> Color.Black
        else -> Color.White
    }
    val borderColor = when {
        isBlack -> Color.DarkGray
        else -> Color.LightGray
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .padding(1.dp)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onKeyPress(note)
                        tryAwaitRelease()
                        isPressed = false
                        onKeyRelease(note)
                    }
                )
            }
    ) {
        Text(note.midiNumberToNote(false).toString(), color = if (isBlack) Color.White else Color.Black)
    }
}

@Preview
@Composable
private fun Preview() {
    PianoView(
        onKeyPress = { key -> println("Key pressed: $key") },
        onKeyRelease = { key -> println("Key released: $key") },
        modifier = Modifier
    )
}