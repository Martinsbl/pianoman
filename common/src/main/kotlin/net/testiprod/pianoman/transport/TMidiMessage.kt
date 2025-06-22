package net.testiprod.pianoman.transport


data class TMidiMessage(
    val status: Int,
    val note: Int?,
    val velocity: Int?,
)