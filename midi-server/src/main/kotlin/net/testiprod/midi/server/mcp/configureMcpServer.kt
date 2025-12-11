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
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import net.testiprod.midi.server.midi.getMidiDeviceInfo
import net.testiprod.midi.server.midi.toMidiMessage
import net.testiprod.midi.server.midi.useMidiDevice
import net.testiprod.pianoman.transport.TMidiMessage
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.midi.server.mcp.McpServer")

private const val ROLAND_PIANO_ID = 1258500461

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
        .addPlayNoteSequenceTool()

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

        useMidiDevice(ROLAND_PIANO_ID) { device ->
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

private fun Server.addPlayNoteSequenceTool(): Server {

    val toolDescription = """
        MIDI Note Sequence Player Tool
        This tool allows you to play a sequence of MIDI notes with timing, enabling you to play melodies, chords, and musical phrases.
        
        Input Parameters:
        - tempo (integer): Beats per minute (BPM). Default: 120
        - notes (array): Array of note events. Each note event is an object with:
            - note (integer): MIDI note number (0-127). Middle C = 60
            - velocity (integer): Note velocity/loudness (0-127). Default: 64
            - duration (integer): Note duration in milliseconds
            - delay (integer): Optional delay before this note (milliseconds). Use to create rests or stagger chord notes.
        
        Note Name to MIDI Number Reference:
        C4 (Middle C) = 60, C#4/Db4 = 61, D4 = 62, D#4/Eb4 = 63, E4 = 64, F4 = 65,
        F#4/Gb4 = 66, G4 = 67, G#4/Ab4 = 68, A4 = 69, A#4/Bb4 = 70, B4 = 71,
        C5 = 72, etc. Each octave adds/subtracts 12.
        
        Example - Simple Melody (C Major Scale):
        {
            "tempo": 120,
            "notes": [
                {"note": 60, "velocity": 80, "duration": 500},
                {"note": 62, "velocity": 80, "duration": 500},
                {"note": 64, "velocity": 80, "duration": 500},
                {"note": 65, "velocity": 80, "duration": 500},
                {"note": 67, "velocity": 80, "duration": 500},
                {"note": 69, "velocity": 80, "duration": 500},
                {"note": 71, "velocity": 80, "duration": 500},
                {"note": 72, "velocity": 80, "duration": 500}
            ]
        }
        
        Example - Chord (C Major):
        {
            "tempo": 120,
            "notes": [
                {"note": 60, "velocity": 70, "duration": 1000, "delay": 0},
                {"note": 64, "velocity": 70, "duration": 1000, "delay": 0},
                {"note": 67, "velocity": 70, "duration": 1000, "delay": 0}
            ]
        }
        
        Example - Moonlight Sonata Opening (First few arpeggiated triplets in C# minor):
        The piece uses triplet eighth notes arpeggiated. At tempo 54, each triplet eighth ≈ 370ms.
        {
            "tempo": 54,
            "notes": [
                {"note": 49, "velocity": 40, "duration": 2960},
                {"note": 61, "velocity": 30, "duration": 370, "delay": 0},
                {"note": 64, "velocity": 30, "duration": 370},
                {"note": 68, "velocity": 30, "duration": 370},
                {"note": 61, "velocity": 30, "duration": 370},
                {"note": 64, "velocity": 30, "duration": 370},
                {"note": 68, "velocity": 30, "duration": 370},
                {"note": 61, "velocity": 30, "duration": 370},
                {"note": 64, "velocity": 30, "duration": 370}
            ]
        }
        
        Tips:
        - For chords, set the same delay (or 0) for all notes in the chord
        - For arpeggios, notes play sequentially after each other's duration
        - Use lower velocity (30-50) for accompaniment, higher (70-100) for melody
        - The bass note often has a longer duration in piano pieces
    """.trimIndent()

    addTool(
        name = "play-note-sequence",
        title = "Play Note Sequence",
        description = toolDescription,
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                putJsonObject("tempo") {
                    put("type", "integer")
                    put("description", "Beats per minute (BPM)")
                    put("default", 120)
                }
                putJsonObject("notes") {
                    put("type", "array")
                    put("description", "Array of note events to play")
                    putJsonObject("items") {
                        put("type", "object")
                        putJsonObject("properties") {
                            putJsonObject("note") {
                                put("type", "integer")
                                put("minimum", 0)
                                put("maximum", 127)
                                put("description", "MIDI note number (0-127)")
                            }
                            putJsonObject("velocity") {
                                put("type", "integer")
                                put("minimum", 0)
                                put("maximum", 127)
                                put("default", 64)
                                put("description", "Note velocity (0-127)")
                            }
                            putJsonObject("duration") {
                                put("type", "integer")
                                put("description", "Note duration in milliseconds")
                            }
                            putJsonObject("delay") {
                                put("type", "integer")
                                put("default", 0)
                                put("description", "Delay before playing this note (milliseconds)")
                            }
                        }
                        putJsonArray("required") {
                            add(kotlinx.serialization.json.JsonPrimitive("note"))
                            add(kotlinx.serialization.json.JsonPrimitive("duration"))
                        }
                    }
                }
            },
            required = listOf("notes")
        )
    ) { request ->
        val tempo = request.arguments?.get("tempo")?.jsonPrimitive?.int ?: 120
        val notesArray = request.arguments?.get("notes") as? JsonArray
            ?: throw IllegalArgumentException("Missing notes array")

        val playedNotes = mutableListOf<String>()
        var errorMessage: String? = null

        useMidiDevice(ROLAND_PIANO_ID) { device ->
            val activeNotes = mutableListOf<Pair<Int, Long>>() // note number to end time

            for (noteJson in notesArray) {
                val noteObj = noteJson.jsonObject
                val noteNumber = noteObj["note"]?.jsonPrimitive?.int
                    ?: throw IllegalArgumentException("Missing note number")
                val velocity = noteObj["velocity"]?.jsonPrimitive?.int ?: 64
                val duration = noteObj["duration"]?.jsonPrimitive?.int
                    ?: throw IllegalArgumentException("Missing duration")
                val delayMs = noteObj["delay"]?.jsonPrimitive?.int ?: 0

                try {
                    // Apply delay before this note
                    if (delayMs > 0) {
                        delay(delayMs.toLong())
                    }

                    // Send Note On
                    val noteOnMessage = TMidiMessage(144, noteNumber, velocity).toMidiMessage()
                    device.receiver.send(noteOnMessage, -1L)
                    playedNotes.add("Note $noteNumber (vel=$velocity, dur=${duration}ms)")

                    // Schedule note off after duration
                    val endTime = System.currentTimeMillis() + duration
                    activeNotes.add(noteNumber to endTime)

                    // Wait for note duration before proceeding to next note
                    // (unless the next note has delay=0, which means it's part of a chord)
                    delay(duration.toLong())

                    // Send Note Off for this note
                    val noteOffMessage = TMidiMessage(128, noteNumber, 0).toMidiMessage()
                    device.receiver.send(noteOffMessage, -1L)

                } catch (e: Exception) {
                    logger.error("Error playing note $noteNumber", e)
                    errorMessage = "Error playing note $noteNumber: ${e.message}"
                    break
                }
            }

            // Ensure all notes are off
            for ((noteNumber, _) in activeNotes) {
                try {
                    val noteOffMessage = TMidiMessage(128, noteNumber, 0).toMidiMessage()
                    device.receiver.send(noteOffMessage, -1L)
                } catch (e: Exception) {
                    logger.warn("Error sending note off for $noteNumber", e)
                }
            }
        }

        val result = if (errorMessage != null) {
            "Error: $errorMessage\nPlayed ${playedNotes.size} notes before error."
        } else {
            "Successfully played ${playedNotes.size} notes at tempo $tempo BPM:\n${playedNotes.joinToString("\n")}"
        }

        CallToolResult(
            content = listOf(TextContent(result))
        )
    }

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
