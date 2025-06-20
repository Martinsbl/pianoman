package net.testiprod.pianoman.midi

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage
import kotlin.math.abs
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("net.testiprod.pianoman.midi")

var connectedDevice: MidiDevice? = null

fun getMidiDeviceInfo(): Array<MidiDevice.Info> {
    val midiDevices = MidiSystem.getMidiDeviceInfo()
    return midiDevices
}

fun connectToMidiDevice(deviceInfo: MidiDevice.Info): MidiDevice? {
    disconnectMidiDevice()
    connectedDevice = MidiSystem.getMidiDevice(deviceInfo)
    logger.info("Connecting to MIDI device: ${deviceInfo.friendlyName()}")
    connectedDevice?.open()
    return connectedDevice
}

fun disconnectMidiDevice() {
    connectedDevice?.let {
        it.close()
        logger.info("Closed MIDI device: ${it.friendlyName()}")
    }
    connectedDevice = null
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
            logger.info("MIDI receiver closed for device: ${device.friendlyName()}")
        }
    }
}


fun MidiDevice.friendlyName(): String {
    return deviceInfo.friendlyName()
}

fun MidiDevice.Info.friendlyName(): String {
    return "$name - $vendor ($description)"
}

fun MidiDevice.Info.getId(): Int {
    return abs((name + vendor + description + version).hashCode())
}