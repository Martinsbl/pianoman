package net.testiprod.pianoman.app.ai

import dev.langchain4j.model.openai.OpenAiChatModel
import java.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.testiprod.pianoman.app.config.AiConfig


class Ai(
    config: AiConfig,
) {

    private val model: OpenAiChatModel by lazy {
        OpenAiChatModel
            .builder()
            .timeout(Duration.ofSeconds(60)) // TODO Increase this if needed
            .baseUrl(config.baseUrl)
            .modelName(config.model)
            .apiKey(config.apiKey)
            .maxTokens(config.maxTokens)
            .temperature(config.temperature)
//            .logRequests(true)
//            .logResponses(true)
            .build()
    }

    suspend fun chat(prompt: String): String? = withContext(Dispatchers.IO) {
        return@withContext model.generate(prompt)
    }

}