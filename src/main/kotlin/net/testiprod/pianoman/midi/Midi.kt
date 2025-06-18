package net.testiprod.pianoman.midi

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiSystem

data class MidiDeviceInfo(
    val name: String,
    val vendor: String,
    val description: String,
    val version: String
)

fun getMidiDeviceInfo(): Array<MidiDevice.Info> {
    val midiDevices = MidiSystem.getMidiDeviceInfo()
    return midiDevices
}