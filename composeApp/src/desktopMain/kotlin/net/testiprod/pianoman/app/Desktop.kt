package net.testiprod.pianoman.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pianoman",
    ) {
        App()
    }
}