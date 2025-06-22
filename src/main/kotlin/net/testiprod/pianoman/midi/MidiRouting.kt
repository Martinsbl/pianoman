package net.testiprod.pianoman.midi

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.sse.sse
import io.ktor.sse.ServerSentEvent
import javax.sound.midi.MidiDevice


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
                    val deviceInfos = devices.map { MidiDeviceInfo(it.getId(), it.name, it.vendor, it.description, it.version) }
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
                     * Catch exceptions and handle them gracefully, e.g., if "MIDI OUT transmitter not available"
                     * Period 'ping' to keep the connection alive, if needed?
                     */
                    val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Missing device ID")
                    val device = getMidiDevice(id)
                    val midiMessageFlow = midiMessagesFlow(device)
                    midiMessageFlow.collect {
                        send(ServerSentEvent(it.toTransport().toString()))
                    }
                }
            }
        }
    }
}

fun getMidiDevice(id: Int): MidiDevice {
    val allDeviceInfo = getMidiDeviceInfo()
    val deviceInfo = allDeviceInfo.firstOrNull { it.getId() == id }
    requireNotNull(deviceInfo) { "No MIDI device found with ID: $id" }
    return connectToMidiDevice(deviceInfo)
}
