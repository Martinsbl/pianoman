package net.testiprod.pianoman.app.ai

import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import java.io.File
import java.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.testiprod.pianoman.app.config.AiConfig
import org.slf4j.LoggerFactory


class Ai(
    config: AiConfig,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

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

    private fun getBaseSystemPrompt(): String {
        val systemPromptPath = "../config/system-prompt.md"
        val systemPromptFile = File(systemPromptPath)
        require(systemPromptFile.exists() && systemPromptFile.isFile) {
            "System prompt file not found at '$systemPromptPath'. Please ensure the file exists and is a valid file."
        }
        return systemPromptFile.readText()
    }

    suspend fun chat(prompt: String): TeacherResponse = withContext(Dispatchers.IO) {
        val systemPrompt = getBaseSystemPrompt()
        val aiResult = aiTeacher.teach(systemPrompt, prompt)
        logger.info(aiResult.tokenUsage().toString())
        return@withContext aiResult.content()
    }

}