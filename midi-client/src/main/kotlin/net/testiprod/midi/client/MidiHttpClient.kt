package net.testiprod.midi.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.testiprod.midi.client.ktor.configureGson
import net.testiprod.pianoman.transport.TMidiDeviceInfo

class MidiHttpClient(
    private val httpClient: HttpClient
) {

    constructor(
        host: String,
        port: Int,
        logLevel: LogLevel = LogLevel.NONE,
    ) : this(
        HttpClient(CIO) {
            defaultRequest {
                url("$host:$port")
            }
            configureGson()
            install(Logging) {
                level = logLevel
                logger = Logger.DEFAULT
            }
        },
    )

    suspend fun getMidiDevices(): NetworkResult<List<TMidiDeviceInfo>, String> = withContext(Dispatchers.IO) {
        return@withContext NetworkResult.doNetworkRequest {
            httpClient.get("/midi/devices")
        }
    }
}