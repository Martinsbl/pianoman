package net.testiprod.midi.server.midi

data class MidiDeviceInfo(
    val id: Int,
    val name: String,
    val vendor: String,
    val description: String,
    val version: String
)