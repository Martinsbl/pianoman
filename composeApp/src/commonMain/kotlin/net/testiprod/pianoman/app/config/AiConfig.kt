package net.testiprod.pianoman.app.config

data class AiConfig(
    val baseUrl: String,
    val apiKey: String,
    val model: String,
    val temperature: Double = 0.7,
    val maxTokens: Int = 1000,
)