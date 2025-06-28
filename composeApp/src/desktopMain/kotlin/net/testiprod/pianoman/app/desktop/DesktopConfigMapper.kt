package net.testiprod.pianoman.app.desktop

import com.typesafe.config.ConfigFactory
import java.io.File
import net.testiprod.pianoman.app.config.AiConfig
import net.testiprod.pianoman.app.config.ConfigMapper
import net.testiprod.pianoman.app.config.MidiServerConfig

class DesktopConfigMapper : ConfigMapper {

    private val defaultPath = "../config/desktop-config.conf"
    private val environmentPath: String? = System.getenv("DESKTOP_CONFIG_PATH")

    private val config by lazy {
        val path = environmentPath ?: defaultPath
        val file = File(path)
        require(file.exists() && file.isFile) {
            "Configuration file not found at '$path'. Please ensure the file exists and is a valid file."
        }
        ConfigFactory.parseFile(file)
    }

    override fun getAiConfig(): AiConfig {
        val aiConfig = config.getConfig("ai")
        val apiConfig = aiConfig.getConfig("api")
        return AiConfig(
            baseUrl = apiConfig.getString("base-url"),
            apiKey = apiConfig.getString("api-key"),
            model = apiConfig.getString("model"),
            temperature = apiConfig.getDouble("temperature"),
            maxTokens = apiConfig.getInt("max-tokens")
        )
    }

    override fun getMidiServerConfig(): MidiServerConfig {
        val midiConfig = config.getConfig("midi.server")
        return MidiServerConfig(
            host = midiConfig.getString("host"),
            port = midiConfig.getInt("port"),
        )
    }
}