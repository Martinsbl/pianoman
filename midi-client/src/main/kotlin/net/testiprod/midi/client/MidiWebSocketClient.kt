package net.testiprod.midi.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.utils.io.core.Closeable
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import net.testiprod.midi.client.ktor.commonGson
import net.testiprod.midi.client.ktor.configureGson
import net.testiprod.pianoman.transport.TMidiMessage
import org.slf4j.LoggerFactory

class MidiWebSocketClient(
    private val client: HttpClient
) : Closeable {

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

    private val logger = LoggerFactory.getLogger(javaClass)

    private val sendChannel = Channel<String>(Channel.UNLIMITED)
    private var sessionJob: Job? = null

    fun connectToTransmitWebSocket(deviceId: Int, scope: CoroutineScope) {
        logger.info("Connecting to TX WebSocket for device $deviceId")
        sessionJob = scope.launch(Dispatchers.IO) {
            client.webSocket("/midi/devices/$deviceId/ws-send") {
                try {
                    for (message in sendChannel) {
                        send(Frame.Text(message))
                    }
                } catch (e: Exception) {
                    logger.error("Error sending message to TX WebSocket: ${e.message}", e)
                } finally {
                    client.close()
                    logger.info("TX WebSocket connection closed for device $deviceId")
                }
            }
        }
    }

    suspend fun sendMidiMessage(message: TMidiMessage) {
        if (sessionJob?.isActive == true) {
            val json = commonGson.toJson(message)
            sendChannel.send(json)
        } else {
            logger.warn("Session is not active, cannot send MIDI message for device")
        }
    }

    fun getMidiMessageFlow(deviceId: Int): Flow<TMidiMessage> = flow {
        logger.info("Connecting to RX WebSocket for device $deviceId")
        try {
            client.webSocket("/midi/devices/$deviceId/ws-receive") {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val json = frame.readText()
                            val tMidiMessage = commonGson.fromJson(json, TMidiMessage::class.java)
                            emit(tMidiMessage)
                        }

                        else -> logger.info("Received non-text frame: $frame")
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error connecting to RX WebSocket for device $deviceId: ${e.message}", e)
        } finally {
            client.close()
            logger.info("RX WebSocket connection closed for device $deviceId")
        }
    }

    override fun close() {
        sendChannel.close()
        sessionJob?.cancel()
    }

}