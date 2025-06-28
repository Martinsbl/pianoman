package net.testiprod.pianoman.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import net.testiprod.pianoman.app.ai.AiViewModel
import net.testiprod.pianoman.app.config.AppConfig
import net.testiprod.pianoman.app.midi.MidiViewModel
import net.testiprod.pianoman.app.ui.screens.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(appConfig: AppConfig) {

    val midiViewModel = viewModel<MidiViewModel>(factory = MidiViewModel.factory(appConfig.midiServer))
    val aiViewModel = viewModel<AiViewModel>(factory = AiViewModel.factory(appConfig.aiConfig))

    MaterialTheme {
        MainScreen(midiViewModel, aiViewModel)
    }
}