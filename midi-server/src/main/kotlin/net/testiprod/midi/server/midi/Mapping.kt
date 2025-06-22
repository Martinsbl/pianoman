package net.testiprod.midi.server.midi

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiMessage
import javax.sound.midi.ShortMessage
import net.testiprod.midi.server.transport.TMidiMessage

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

fun TMidiMessage.toMidiMessage(): MidiMessage {
    return when {
        note == null && velocity == null -> ShortMessage(status)
        note != null && velocity != null -> ShortMessage(status, note, velocity)
        else -> {
            throw IllegalArgumentException("Both note and velocity must be provided or neither")
        }
    }
}