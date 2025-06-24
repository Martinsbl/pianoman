package net.testiprod.pianoman.app.ui

sealed class UiState<out T: Any> {
    data class Success<out T: Any>(val data: T?) : UiState<T>()
    data class Error(val title: String, val message: String?) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}
