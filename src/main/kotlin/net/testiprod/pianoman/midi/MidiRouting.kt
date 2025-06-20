package net.testiprod.pianoman.midi

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.sse.sse
import io.ktor.sse.ServerSentEvent


fun Route.configureMidiRouting() {
    route("/midi") {
        get("/devices") {
            val devices = getMidiDeviceInfo()
            val domainDevices = devices.map { it.toDomain() }
            call.respond(domainDevices)
        }
        post("/connect/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Missing device ID")
            val allDeviceInfo = getMidiDeviceInfo()
            val deviceInfo = allDeviceInfo.firstOrNull { it.getId() == id }
            requireNotNull(deviceInfo) { "No MIDI device found with ID: $id" }
            connectToMidiDevice(deviceInfo)
            call.respond("Connected to MIDI device $id: ${deviceInfo.friendlyName()}")

        }
        sse("/events") {
            val device = requireNotNull(connectedDevice) { "No MIDI device connected" }
            val midiMessageFlow = midiMessagesFlow(device)
            midiMessageFlow.collect {
                send(ServerSentEvent(it.message.joinToString(", ")))
            }
        }
    }
}
