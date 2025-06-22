package net.testiprod.pianoman.transport

data class TMidiDeviceInfo(
    val id: Int,
    val name: String,
    val vendor: String,
    val description: String,
    val version: String
)