package net.testiprod.pianoman.app.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun AiView(
    aiResponseState: UiState<String>,
    onPromptClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var prompt: String by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        UiStateView(aiResponseState) { response ->
            Text(
                text = response,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Enter your prompt") },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            enabled = prompt.isNotBlank(),
            onClick = {
                onPromptClick(prompt)
            }) {
            Text("Prompt")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Scaffold {
        AiView(
            UiState.Success("Hello, AI!"),
            onPromptClick = { _ -> },
        )
    }
}