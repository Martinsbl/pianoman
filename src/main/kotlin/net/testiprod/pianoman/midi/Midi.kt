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


fun getMidiDeviceInfo(): Array<MidiDevice.Info> {
    val midiDevices = MidiSystem.getMidiDeviceInfo()
    return midiDevices
}

suspend fun useMidiDevice(id: Int, block: suspend (MidiDevice) -> Unit) {
    val deviceInfo = getMidiDeviceInfo().firstOrNull { it.getId() == id }
    requireNotNull(deviceInfo) { "No MIDI device found with ID: $id" }
    val device = openMidiDevice(deviceInfo)
    try {
        block(device)
    } catch (e: Exception) {
        logger.warn("Error using MIDI device with ID $id", e)
        throw e
    } finally {
        closeMidiDevice(device)
    }
}

fun getMidiDevice(id: Int): MidiDevice {
    val allDeviceInfo = getMidiDeviceInfo()
    val deviceInfo = allDeviceInfo.firstOrNull { it.getId() == id }
    requireNotNull(deviceInfo) { "No MIDI device found with ID: $id" }
    return openMidiDevice(deviceInfo)
}

private fun openMidiDevice(deviceInfo: MidiDevice.Info): MidiDevice {
    val device = MidiSystem.getMidiDevice(deviceInfo)
    device.open()
    logger.info("Connected to MIDI device: ${deviceInfo.friendlyName()}")
    return device
}

fun closeMidiDevice(connectedDevice: MidiDevice) {
    connectedDevice.close()
    logger.info("Closed MIDI device: ${connectedDevice.friendlyName()}")
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