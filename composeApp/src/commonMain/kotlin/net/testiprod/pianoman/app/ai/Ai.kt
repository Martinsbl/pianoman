package net.testiprod.pianoman.app.ai

import dev.langchain4j.model.openai.OpenAiChatModel
import net.testiprod.pianoman.app.config.AiConfig


class Ai(
    private val config: AiConfig,
) {

    var model: OpenAiChatModel = OpenAiChatModel.builder()
        .baseUrl(config.baseUrl)
        .apiKey(config.apiKey)
        .modelName(config.model)
        .temperature(config.temperature)
        .maxTokens(config.maxTokens)
        .build()


}