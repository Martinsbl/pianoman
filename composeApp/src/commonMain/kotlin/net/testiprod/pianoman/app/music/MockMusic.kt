package net.testiprod.pianoman.app.music

data class MidiTimings(
    val note: Int,
    val startTime: Long,
)

val moonlightSonataNotes = listOf(
    MidiTimings("C#4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("E4".toNote().toMidiNoteNumber(), 400),
    MidiTimings("G#4".toNote().toMidiNoteNumber(), 800),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 1200),
    MidiTimings("E5".toNote().toMidiNoteNumber(), 1600),
    MidiTimings("G#5".toNote().toMidiNoteNumber(), 2000),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 2400),
    MidiTimings("E4".toNote().toMidiNoteNumber(), 2800),
    MidiTimings("G#4".toNote().toMidiNoteNumber(), 3200),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 3600),
    MidiTimings("E5".toNote().toMidiNoteNumber(), 4000),
    MidiTimings("G#5".toNote().toMidiNoteNumber(), 4400),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 4800),
    MidiTimings("E4".toNote().toMidiNoteNumber(), 5200),
    MidiTimings("G#4".toNote().toMidiNoteNumber(), 5600),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 6000),
    MidiTimings("E5".toNote().toMidiNoteNumber(), 6400),
    MidiTimings("G#5".toNote().toMidiNoteNumber(), 6800),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 7200),
    MidiTimings("E4".toNote().toMidiNoteNumber(), 7600),
    MidiTimings("G#4".toNote().toMidiNoteNumber(), 8000),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 8400),
    MidiTimings("E5".toNote().toMidiNoteNumber(), 8800),
    MidiTimings("G#5".toNote().toMidiNoteNumber(), 9200),
    MidiTimings("C#5".toNote().toMidiNoteNumber(), 9600),
)

val chordProgression = listOf(
    MidiTimings("C4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("E4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("G4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("C4".toNote().toMidiNoteNumber(), 1000),
    MidiTimings("E4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("A4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("C5".toNote().toMidiNoteNumber(), 0),
    MidiTimings("F4".toNote().toMidiNoteNumber(), 1000),
    MidiTimings("A4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("C5".toNote().toMidiNoteNumber(), 0),
    MidiTimings("G4".toNote().toMidiNoteNumber(), 1000),
    MidiTimings("B4".toNote().toMidiNoteNumber(), 0),
    MidiTimings("D5".toNote().toMidiNoteNumber(), 0),
)