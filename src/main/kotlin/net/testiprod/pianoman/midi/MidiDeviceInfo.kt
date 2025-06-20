package net.testiprod.pianoman.midi

data class MidiDeviceInfo(
    val id: Int,
    val name: String,
    val vendor: String,
    val description: String,
    val version: String
)