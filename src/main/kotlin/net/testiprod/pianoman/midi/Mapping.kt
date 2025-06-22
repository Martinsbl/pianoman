package net.testiprod.pianoman.midi

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiMessage
import net.testiprod.pianoman.transport.TMidiMessage

fun MidiDevice.Info.toDomain(): MidiDeviceInfo {
    return MidiDeviceInfo(
        id = this.getId(),
        name = this.name,
        vendor = this.vendor,
        description = this.description,
        version = this.version
    )
}

fun MidiMessage.toTransport(): TMidiMessage {
    return TMidiMessage(
        status = status,
        note = message.getOrNull(1)?.toInt(),
        velocity = message.getOrNull(2)?.toInt(),
    )
}