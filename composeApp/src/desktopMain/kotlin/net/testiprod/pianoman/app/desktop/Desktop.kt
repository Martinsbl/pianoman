package net.testiprod.pianoman.app.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.testiprod.pianoman.app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pianoman",
    ) {
        App()
    }
}