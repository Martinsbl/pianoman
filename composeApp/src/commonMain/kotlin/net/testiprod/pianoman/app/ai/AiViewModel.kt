package net.testiprod.pianoman.app.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.testiprod.pianoman.app.config.AiConfig
import net.testiprod.pianoman.app.ui.UiState
import org.slf4j.LoggerFactory

class AiViewModel(
    aiConfig: AiConfig,
) : ViewModel() {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val ai = Ai(aiConfig)

    private val _aiResponse = MutableStateFlow<UiState<String>>(UiState.Loading)
    val aiResponse: StateFlow<UiState<String>> = _aiResponse.asStateFlow()


    fun chat(prompt: String) {
        _aiResponse.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = ai.chat(prompt)
                logger.info("AI response {}", response)
                val chords = response.answerChord.joinToString(", ")
                val formattedResponse = "Question: ${response.chordQuestion}\nChords: $chords"
                _aiResponse.value = UiState.Success(formattedResponse)
            } catch (e: Exception) {
                logger.error("Error during AI chat", e)
                _aiResponse.value = UiState.Error(e::class.java.simpleName, e.message ?: "Unknown error")
            }
        }
    }

    companion object {

        fun factory(
            aiConfig: AiConfig,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                    @Suppress("UNCHECKED_CAST")
                    return AiViewModel(aiConfig) as T
                }
            }
    }
}