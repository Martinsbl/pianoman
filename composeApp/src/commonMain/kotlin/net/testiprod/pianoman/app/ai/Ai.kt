package net.testiprod.pianoman.app.ai

import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
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
            .timeout(Duration.ofSeconds(60))
            .baseUrl(config.baseUrl)
            .modelName(config.model)
            .apiKey(config.apiKey)
            .maxTokens(config.maxTokens)
            .temperature(config.temperature)
            .strictJsonSchema(true)
            .logRequests(true)
            .build()
    }

    private val aiTeacher = AiServices.builder(AiTeacher::class.java)
        .chatLanguageModel(model)
        .build()

    suspend fun chat(prompt: String): TeacherResponse = withContext(Dispatchers.IO) {
        return@withContext aiTeacher.teach(prompt)
    }

}