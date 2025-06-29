package net.testiprod.pianoman.app.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import net.testiprod.pianoman.app.App
import net.testiprod.pianoman.app.config.AppConfig

fun main() = application {
    val appConfig = AppConfig.getConfig(DesktopConfigMapper())

    Window(
        onCloseRequest = ::exitApplication,
        title = "Pianoman",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
    ) {
        App(appConfig)
    }
}