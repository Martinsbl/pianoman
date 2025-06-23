package net.testiprod.midi.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.testiprod.midi.client.ktor.commonGson
import net.testiprod.midi.client.ktor.configureGson
import net.testiprod.pianoman.transport.TMidiMessage
import org.slf4j.LoggerFactory

class MidiWebSocketClient(
    private val client: HttpClient
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    constructor(host: String, port: Int) : this(
        HttpClient(CIO) {
            install(WebSockets) {
                pingInterval = 5.seconds
            }
            defaultRequest {
                url("$host:$port")
            }
            configureGson()
        },
    )

    fun getMidiMessageFlow(deviceId: Int): Flow<TMidiMessage> = flow {
        logger.info("Connecting to WebSocket for device $deviceId")
        try {
            client.webSocket("/midi/devices/$deviceId/ws-receive") {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val json = frame.readText()
                            logger.info("Received text frame: $json")
//                            val tMidiMessage = commonGson.fromJson(json, TMidiMessage::class.java)
                        }

                        else -> logger.info("Received non-text frame: $frame")
                    }
                }
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
        } finally {
            client.close()
            logger.info("WebSocket connection closed for device $deviceId")
        }
    }

}