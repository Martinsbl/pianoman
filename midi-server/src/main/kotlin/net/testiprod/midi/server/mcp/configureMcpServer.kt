package net.testiprod.midi.server.mcp

import io.ktor.server.application.ApplicationCall
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.Icon
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import io.modelcontextprotocol.kotlin.sdk.types.ToolSchema
import javax.sound.midi.MidiMessage
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import net.testiprod.midi.server.midi.getMidiDeviceInfo
import net.testiprod.midi.server.midi.toMidiMessage
import net.testiprod.midi.server.midi.useMidiDevice
import net.testiprod.pianoman.transport.TMidiMessage
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.midi.server.mcp.McpServer")

fun ApplicationCall.configureMcpServer(): Server {
    val server = Server(
        serverInfo = Implementation(
            name = "midi-mcp-server",
            title = "MIDI MCP Server",
            version = "1.0.0",
            icons = listOf(Icon(src = "https://picsum.photos/48/48")),
        ),
        options = ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(
                    listChanged = true,
                )
            )
        )
    )

    server.addListMidiDevicesTool()
        .addPlayMidiNoteTool()

    return server
}

private fun Server.addPlayMidiNoteTool(): Server {


    val toolDescription = """
            MIDI Note Player Tool
            This tool allows you to play a MIDI note on a MIDI device by sending a MIDI message.
            Input Parameters:
            - status (integer): The status byte of the MIDI message (e.g., 144 for Note On and 128 for Note Off).
            - note (integer): The MIDI note number (0-127).
            - velocity (integer): The velocity of the note (0-127).
            Example Input:
            {
                "status": 144,
                "note": 60,
                "velocity": 100
            }
            This will play the middle C note (MIDI note number 60) with a velocity of 100.
            Example Output:
            Sent MIDI message: TMidiMessage(status=144, note=60, velocity=100
            
            Example 2 Input:
            {
                "status": 128,
                "note": 60,
                "velocity": 0
            }
            This will stop playing the middle C note.
            Example 2 Output:
            Sent MIDI message: TMidiMessage(status=128, note=60, velocity=0
        """.trimIndent()

    // Input schema = MIDI note parameters (status, note number, velocity) as in TMidiMessage
    addTool(
        name = "play-midi-note",
        title = "Play MIDI Note",
        description = toolDescription,
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                putJsonObject("note") {
                    putJsonObject("type") {
                        put("type", "integer")
                        put("minimum", 0)
                        put("maximum", 127)
                    }
                }
                putJsonObject("velocity") {
                    putJsonObject("type") {
                        put("type", "integer")
                        put("minimum", 0)
                        put("maximum", 127)
                    }
                }
                putJsonObject("status") {
                    putJsonObject("type") {
                        put("type", "integer")
                        put("minimum", 128)
                        put("maximum", 255)
                    }
                }
            }
        )
    ) { request ->
        val status = request.arguments?.get("status")?.jsonPrimitive?.content?.toInt()
            ?: throw IllegalArgumentException("Missing status")
        val note = request.arguments?.get("note")?.jsonPrimitive?.content?.toInt()
            ?: throw IllegalArgumentException("Missing note")
        val velocity = request.arguments?.get("velocity")?.jsonPrimitive?.content?.toInt()
            ?: throw IllegalArgumentException("Missing velocity")


        var result = ""

        useMidiDevice(1258500461) { device ->
            val tMidiMessage = TMidiMessage(status, note, velocity)
            val midiMessage: MidiMessage = tMidiMessage.toMidiMessage()
            logger.info(midiMessage.toString())
            try {
                device.receiver.send(midiMessage, -1L)
                result = "Sent MIDI message: $tMidiMessage"
            } catch (e: Exception) {
                logger.error("Error processing MIDI event: $midiMessage", e)
                result = "Error sending MIDI message: ${e.message}"
            }

        }
        CallToolResult(
            content = listOf(TextContent(result))
        )

    }


// Placeholder for future tool to play MIDI notes
    return this
}

private fun Server.addListMidiDevicesTool(): Server {

    addTool(
        name = "list-midi-devices",
        title = "List MIDI Devices",
        description = "Lists available MIDI devices on the server.",
    ) {

        val devices = getMidiDeviceInfo()

        CallToolResult(
            content = devices.map {
                TextContent("• ${it.name} (${it.vendor})")
            }
        )
    }

    return this
}
