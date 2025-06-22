package net.testiprod.midi.server

import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.ApplicationConfigValue

data class Server(
    val port: Int,
    val host: String,
)

data class AppConfig(
    val server: Server,
) {
    companion object {
        fun fromFile(filePath: String = "application.yaml"): AppConfig {
            val config = ApplicationConfig(filePath)

            val ktorConfig = config.config("ktor")
            val serverConfig = ktorConfig.config("server")

            return AppConfig(
                server = Server(
                    port = config.property("ktor.server.port").toInt(),
                    host = serverConfig.property("host").getString()
                )
            )
        }
    }
}

private fun ApplicationConfigValue.toInt(): Int {
    return this.getString().toIntOrNull() ?: throw IllegalArgumentException("Invalid integer value: ${this.getString()}")
}