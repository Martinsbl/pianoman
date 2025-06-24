package net.testiprod.pianoman.app

import net.testiprod.midi.client.NetworkResult
import net.testiprod.pianoman.app.ui.UiState

fun <S : Any> NetworkResult<S, *>.toUiState(): UiState<S> {
    return when (this) {
        is NetworkResult.Success -> this.toUiStateData()
        is NetworkResult.Error -> this.toUiStateError()
    }
}

fun <S : Any> NetworkResult.Success<S>.toUiStateData(): UiState.Success<S> {
    return UiState.Success(data)
}

fun NetworkResult.Error<*>.toUiStateError(): UiState.Error {
    return UiState.Error(
        title = simpleErrorMessage,
        message = advancedErrorMessage,
    )
}