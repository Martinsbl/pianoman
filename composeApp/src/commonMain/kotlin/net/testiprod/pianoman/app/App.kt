package net.testiprod.pianoman.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import net.testiprod.pianoman.app.config.AppConfig
import net.testiprod.pianoman.app.ui.screens.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(appConfig: AppConfig) {
    MaterialTheme {
        MainScreen(appConfig)
    }
}