package net.testiprod.midi.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.testiprod.midi.client.ktor.configureGson
import net.testiprod.pianoman.transport.TMidiDeviceInfo

class MidiHttpClient(
    private val httpClient: HttpClient
) {

    constructor(host: String, port: Int) : this(
        HttpClient(CIO) {
            defaultRequest {
                url("$host:$port")
            }
            configureGson()
        },
    )

    suspend fun getMidiDevices(): NetworkResult<List<TMidiDeviceInfo>, String> = withContext(Dispatchers.IO) {
        return@withContext NetworkResult.doNetworkRequest {
            httpClient.get("/midi/devices")
        }
    }
}