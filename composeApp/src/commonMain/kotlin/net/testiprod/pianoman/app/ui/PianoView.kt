package net.testiprod.pianoman.app.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PianoView(
    onKeyPress: (Int) -> Unit,
    onKeyRelease: (Int) -> Unit,
    modifier: Modifier = Modifier,
    keys: List<Int> = (60..72).toList(), // Default to 88 keys
) {

    Row(modifier = modifier) {
        keys.forEach { key ->
            KeyView(
                key = key,
                onKeyPress = onKeyPress,
                onKeyRelease = onKeyRelease,
                modifier = Modifier.weight(1f)
            )
        }
    }

}

@Composable
private fun KeyView(
    key: Int,
    onKeyPress: (Int) -> Unit,
    onKeyRelease: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Draw a square or rectangle to represent the key, and detect clicks and releases
    Button(
        onClick = { onKeyPress(key) },
        modifier = modifier
    ) {
        // Here you can customize the appearance of the key
        // For example, you can use Text to show the key number
         Text(text = "$key")
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