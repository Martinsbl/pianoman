package net.testiprod.pianoman.app.music

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NoteTest {

    @Test
    fun `it throws on invalid note strings`() {
        assertFailsWith<IllegalArgumentException> {
            Note.fromString("C")
        }
        assertFailsWith<IllegalArgumentException> {
            Note.fromString("K")
        }
        assertFailsWith<IllegalArgumentException> {
            Note.fromString("CC")
        }
        assertFailsWith<IllegalArgumentException> {
            Note.fromString("C$")
        }
        assertFailsWith<IllegalArgumentException> {
            Note.fromString("ABCD")
        }
    }

    @Test
    fun `it converts strings to Notes`() {
        val noteC = Note.fromString("C4")
        assertEquals(Note(NoteLetter.C, 4), noteC)
        val noteCsharp = Note.fromString("C#4")
        assertEquals(Note(NoteLetter.C, 4, Accidental.SHARP), noteCsharp)
        val noteD = Note.fromString("D4")
        assertEquals(Note(NoteLetter.D, 4), noteD)
        val noteDSharp = Note.fromString("D#3")
        assertEquals(Note(NoteLetter.D, 3, Accidental.SHARP), noteDSharp)
        val noteBFlat = Note.fromString("Bb5")
        assertEquals(Note(NoteLetter.B, 5, Accidental.FLAT), noteBFlat)
        val noteA = Note.fromString("A0")
        assertEquals(Note(NoteLetter.A, 0), noteA)
    }

    @Test
    fun `it converts Notes to strings`() {
        val noteC = Note(NoteLetter.C, 4)
        assertEquals("C4", noteC.toString())
        val noteCsharp = Note(NoteLetter.C, 4, Accidental.SHARP)
        assertEquals("C#4", noteCsharp.toString())
        val noteD = Note(NoteLetter.D, 4)
        assertEquals("D4", noteD.toString())
        val noteDSharp = Note(NoteLetter.D, 3, Accidental.SHARP)
        assertEquals("D#3", noteDSharp.toString())
        val noteBFlat = Note(NoteLetter.B, 5, Accidental.FLAT)
        assertEquals("B♭5", noteBFlat.toString())
        val noteA = Note(NoteLetter.A, 0)
        assertEquals("A0", noteA.toString())
    }

    @Test
    fun `it converts Notes to MIDI numbers`() {
        val noteC = Note(NoteLetter.C, 4)
        assertEquals(60, noteC.toMidiNumber())
        val noteCsharp = Note(NoteLetter.C, 4, Accidental.SHARP)
        assertEquals(61, noteCsharp.toMidiNumber())
        val noteD = Note(NoteLetter.D, 4)
        assertEquals(62, noteD.toMidiNumber())
        val noteDSharp = Note(NoteLetter.D, 3, Accidental.SHARP)
        assertEquals(51, noteDSharp.toMidiNumber())
        val noteBFlat = Note(NoteLetter.B, 5, Accidental.FLAT)
        assertEquals(82, noteBFlat.toMidiNumber())
        val noteA = Note(NoteLetter.A, 0)
        assertEquals(21, noteA.toMidiNumber())
    }

    @Test
    fun `it converts MIDI numbers to Notes`() {
        assertEquals(Note(NoteLetter.C, 4), Note.fromMidi(60))
        assertEquals(Note(NoteLetter.C, 4, Accidental.SHARP), Note.fromMidi(61))
        assertEquals(Note(NoteLetter.D, 4), Note.fromMidi(62))
        assertEquals(Note(NoteLetter.D, 3, Accidental.SHARP), Note.fromMidi(51))
        assertEquals(Note(NoteLetter.B, 5, Accidental.FLAT), Note.fromMidi(82, preferSharps = false))
        assertEquals(Note(NoteLetter.A, 0), Note.fromMidi(21))
    }

    @Test
    fun `it handles transposing`() {
        val noteC = Note(NoteLetter.C, 4)
        assertEquals(Note(NoteLetter.D, 4), noteC.transpose(2))
        assertEquals(Note(NoteLetter.B, 3, Accidental.FLAT), noteC.transpose(-2, preferSharps = false))
        assertEquals(Note(NoteLetter.C, 5), noteC.transpose(12))
        assertEquals(Note(NoteLetter.C, 3), noteC.transpose(-12))

        val noteDSharp = Note(NoteLetter.D, 3, Accidental.SHARP)
        assertEquals(Note(NoteLetter.F, 3), noteDSharp.transpose(2))
        assertEquals(Note(NoteLetter.C, 3, Accidental.SHARP), noteDSharp.transpose(-2))
    }
}