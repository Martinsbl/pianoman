package net.testiprod.pianoman.app.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> UiStateView(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        when (state) {
            is UiState.Success -> content(state.data)
            is UiState.Error -> ErrorView(state.title, state.message)
            is UiState.Loading -> CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Scaffold {
        Column {
            UiStateView(
                state = UiState.Success("Hello, World!"),
            ) {
                Text(it)
            }
            UiStateView(
                state = UiState.Loading,
            ) {
            }
            UiStateView(
                state = UiState.Error("Error Title", "An error occurred."),
            ) {
            }
        }
    }
}