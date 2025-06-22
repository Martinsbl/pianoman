package net.testiprod.midi.server.transport


data class TMidiMessage(
    val status: Int,
    val note: Int?,
    val velocity: Int?,
)