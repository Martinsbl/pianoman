package net.testiprod.pianoman.midi

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
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
        sse("/sse") {
            send(ServerSentEvent("sse"))
        }
    }
}
