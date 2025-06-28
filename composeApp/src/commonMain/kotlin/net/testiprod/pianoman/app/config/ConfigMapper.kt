package net.testiprod.pianoman.app.config

interface ConfigMapper {
    fun getAiConfig(): AiConfig
    fun getMidiServerConfig(): MidiServerConfig
}