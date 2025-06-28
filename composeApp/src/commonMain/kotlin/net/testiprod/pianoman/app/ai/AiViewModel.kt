package net.testiprod.pianoman.app.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlinx.coroutines.launch
import net.testiprod.pianoman.app.config.AiConfig
import org.slf4j.LoggerFactory

class AiViewModel(
    aiConfig: AiConfig,
) : ViewModel() {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val ai = Ai(aiConfig)

//    private val _deviceListState = MutableStateFlow<UiState<List<TMidiDeviceInfo>>>(UiState.Loading)
//    val deviceListState: StateFlow<UiState<List<TMidiDeviceInfo>>> = _deviceListState.asStateFlow()


    fun chat(prompt: String) {
        viewModelScope.launch {
            val response = ai.chat(prompt)
            logger.info("AI response {}", response)
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