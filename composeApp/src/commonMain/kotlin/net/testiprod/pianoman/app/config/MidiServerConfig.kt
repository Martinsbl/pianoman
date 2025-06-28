package net.testiprod.pianoman.app.config

data class MidiServerConfig(
    val host: String,
    val port: Int,
    val favoriteMidiId: Int,
)