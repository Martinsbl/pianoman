package net.testiprod.pianoman.app.config

data class AppConfig(
    val midiServer: MidiServerConfig,
    val aiConfig: AiConfig,
) {
    companion object {
        fun getConfig(mapper: ConfigMapper): AppConfig {
            return AppConfig(
                midiServer = mapper.getMidiServerConfig(),
                aiConfig = mapper.getAiConfig()
            )
        }
    }
}