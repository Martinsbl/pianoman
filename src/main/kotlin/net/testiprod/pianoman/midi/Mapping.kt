package net.testiprod.pianoman.midi

import javax.sound.midi.MidiDevice

fun MidiDevice.Info.toDomain(): MidiDeviceInfo {
    return MidiDeviceInfo(
        name = this.name,
        vendor = this.vendor,
        description = this.description,
        version = this.version
    )
}