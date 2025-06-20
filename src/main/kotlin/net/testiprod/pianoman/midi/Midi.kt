package net.testiprod.pianoman.midi


import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Receiver
import kotlin.math.abs
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("net.testiprod.pianoman.midi")

var connectedDevice: MidiDevice? = null
var midiMessageFlow: Flow<MidiMessage>? = null

fun getMidiDeviceInfo(): Array<MidiDevice.Info> {
    val midiDevices = MidiSystem.getMidiDeviceInfo()
    return midiDevices
}

fun connectToMidiDevice(deviceInfo: MidiDevice.Info): MidiDevice {
    disconnectMidiDevice()
    logger.info("Connecting to MIDI device: ${deviceInfo.friendlyName()}")
    MidiSystem.getMidiDevice(deviceInfo).also { device ->
        device.open()
        midiMessageFlow = midiMessagesFlow(device)
        connectedDevice = device
        return device
    }
}

fun disconnectMidiDevice() {
    connectedDevice?.let {
        it.close()
        logger.info("Closed MIDI device: ${it.friendlyName()}")
    }
    connectedDevice = null
}

fun midiMessagesFlow(device: MidiDevice): Flow<MidiMessage> = callbackFlow {
    val receiver = object : Receiver {
        override fun send(message: MidiMessage, timeStamp: Long) {
            logger.debug("MIDI message received: ${message.message.joinToString(", ")}")
            trySend(message)
        }

        override fun close() {
            logger.info("MIDI receiver closed for device: ${device.friendlyName()}")
        }
    }

    val transmitter = device.transmitter
    transmitter.receiver = receiver
    logger.info("MIDI transmitter set up for device: ${device.friendlyName()}")

    awaitClose {
        transmitter.receiver.close()
        transmitter.close()
        logger.info("MIDI transmitter closed for device: ${device.friendlyName()}")
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