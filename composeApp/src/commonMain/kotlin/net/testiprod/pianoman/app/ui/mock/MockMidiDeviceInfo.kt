package net.testiprod.pianoman.app.ui.mock

import net.testiprod.pianoman.transport.TMidiDeviceInfo

val mockMidiDeviceInfo0 = TMidiDeviceInfo(id = 1791575921, name = "Gervill", vendor = "OpenJDK", description = "Software MIDI Synthesizer", version = "1.0")
val mockMidiDeviceInfo1 = TMidiDeviceInfo(
    id = 1219393805,
    name = "Real Time Sequencer",
    vendor = "Oracle Corporation",
    description = "Software sequencer",
    version = "Version 1.0"
)
val mockMidiDeviceInfo2 = TMidiDeviceInfo(
    id = 1110839041,
    name = "Microsoft MIDI Mapper",
    vendor = "Unknown vendor",
    description = "Windows MIDI_MAPPER",
    version = "5.0"
)
val mockMidiDeviceInfo3 = TMidiDeviceInfo(
    id = 85360850,
    name = "Microsoft GS Wavetable Synth",
    vendor = "Unknown vendor",
    description = "Internal software synthesizer",
    version = "1.0"
)
val mockMidiDeviceInfo = listOf(
    mockMidiDeviceInfo0,
    mockMidiDeviceInfo1,
    mockMidiDeviceInfo2,
    mockMidiDeviceInfo3,
)