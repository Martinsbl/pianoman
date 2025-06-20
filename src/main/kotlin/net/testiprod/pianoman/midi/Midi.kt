package net.testiprod.pianoman.midi

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage

var connectedDevice: MidiDevice? = null

fun MidiDevice.Info.getId(): Int {
    return (name + vendor + description + version).hashCode()
}

fun getMidiDeviceInfo(): Array<MidiDevice.Info> {
    val midiDevices = MidiSystem.getMidiDeviceInfo()
    return midiDevices
}

fun connectToMidiDevice(deviceInfo: MidiDevice.Info): MidiDevice? {
    connectedDevice?.close()
    connectedDevice = MidiSystem.getMidiDevice(deviceInfo)
    connectedDevice?.open()
    return connectedDevice
}

fun listenToMidiEvents(device: MidiDevice) {
    val transmitter = device.transmitter
    transmitter.receiver = object : Receiver {
        override fun send(message: MidiMessage, timeStamp: Long) {
            when (message) {
                is ShortMessage -> {
                    when (message.command) {
                        ShortMessage.NOTE_ON -> {
                            val note = message.data1
                            val velocity = message.data2
                            println("Note ON: note=$note, velocity=$velocity")
                        }

                        ShortMessage.NOTE_OFF -> {
                            val note = message.data1
                            println("Note OFF: note=$note")
                        }
                    }
                }
            }
        }

        override fun close() {
            // Cleanup when receiver is closed
        }
    }
}
