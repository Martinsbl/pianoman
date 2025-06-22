package net.testiprod.midi.server.midi

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.sse.sse
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.awaitCancellation
import net.testiprod.pianoman.transport.TMidiDeviceInfo
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.pianoman.midi.MidiHttpRouting")

fun Route.configureMidiRouting() {
    route("/midi") {
        route("/devices") {
            /**
             * Endpoint to get a list of all available MIDI devices.
             */
            get {
                val devices = getMidiDeviceInfo()
                val domainDevices = devices.map { it.toDomain() }
                call.respond(domainDevices)
            }
            route("/{id}") {
                configureWebSockets()
                /**
                 * Endpoint to get information about a specific MIDI device by ID.
                 * @param id The ID of the MIDI device.
                 */
                get {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Missing or invalid device ID")
                    val devices = getMidiDeviceInfo()
                    val deviceInfos = devices.map { TMidiDeviceInfo(it.getId(), it.name, it.vendor, it.description, it.version) }
                    val deviceInfo = deviceInfos.firstOrNull { it.id == id }
                    if (deviceInfo == null) {
                        call.respond(HttpStatusCode.NotFound, "No MIDI device found with ID '$id'")
                    } else {
                        call.respond(deviceInfo)
                    }
                }
                /**
                 * Endpoint to connect to a specific MIDI device by ID.
                 * @param id The ID of the MIDI device to connect to.
                 */
                post("/connect") {
                    val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Missing device ID")
                    val device = getMidiDevice(id)
                    call.respond("Connected to MIDI device '${device.friendlyName()}'")
                }
                /**
                 * Endpoint used for connecting to and get Server-Sent Events (SSE) for MIDI messages from the connected device.
                 */
                sse("/events") {
                    /**
                     * TODO
                     * Better "wait for client to disconnect" logic?
                     * Period 'ping' to keep the connection alive, if needed?
                     */
                    val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Missing device ID")
                    useMidiDevice(id) { device ->
                        val receiver = HttpMidiReceiver(this)
                        device.transmitter.receiver = receiver
                        try {
                            // Wait for the client to close the connection, then move on
                            awaitCancellation()
                        } catch (e: CancellationException) {
                            logger.warn(e.message)
                        }
                    }
                }
            }
        }
    }
}
