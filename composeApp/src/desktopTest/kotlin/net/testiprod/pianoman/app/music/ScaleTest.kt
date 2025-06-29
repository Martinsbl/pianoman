package net.testiprod.pianoman.app.music

import kotlin.test.Test
import kotlin.test.assertEquals

class ScaleTest {

    @Test
    fun `it creates a scale`() {
        val scaleCmajor = Scale(Note.fromString("C4"), ScaleTypes.MAJOR)
        assertEquals(8, scaleCmajor.notes.size)
        var expectedNotes = listOf("C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5").map(Note::fromString)
        assert(scaleCmajor.notes == expectedNotes) { "Expected notes: $expectedNotes, but got: ${scaleCmajor.notes}" }

        val scaleFMajor = Scale(Note.fromString("F4"), ScaleTypes.MAJOR, preferSharps = false)
        assertEquals(8, scaleFMajor.notes.size)
        expectedNotes = listOf("F4", "G4", "A4", "Bb4", "C5", "D5", "E5", "F5").map(Note::fromString)
        assert(scaleFMajor.notes == expectedNotes) { "Expected notes: $expectedNotes, but got: ${scaleFMajor.notes}" }

        val scaleGMajor = Scale(Note.fromString("G4"), ScaleTypes.MAJOR)
        assertEquals(8, scaleGMajor.notes.size)
        expectedNotes = listOf("G4", "A4", "B4", "C5", "D5", "E5", "F#5", "G5").map(Note::fromString)
        assert(scaleGMajor.notes == expectedNotes) { "Expected notes: $expectedNotes, but got: ${scaleGMajor.notes}" }

        val scaleAMajor = Scale(Note.fromString("A4"), ScaleTypes.MAJOR)
        assertEquals(8, scaleAMajor.notes.size)
        expectedNotes = listOf("A4", "B4", "C#5", "D5", "E5", "F#5", "G#5", "A5").map(Note::fromString)

        val scaleAMinor = Scale(Note.fromString("A4"), ScaleTypes.MINOR)
        assertEquals(8, scaleAMinor.notes.size)
        expectedNotes = listOf("A4", "B4", "C5", "D5", "E5", "F5", "G5", "A5").map(Note::fromString)
        assert(scaleAMinor.notes == expectedNotes) { "Expected notes: $expectedNotes, but got: ${scaleAMinor.notes}" }

        val scaleEMinor = Scale(Note.fromString("E4"), ScaleTypes.MINOR)
        assertEquals(8, scaleEMinor.notes.size)
        expectedNotes = listOf("E4", "F#4", "G4", "A4", "B4", "C5", "D5", "E5").map(Note::fromString)
        assert(scaleEMinor.notes == expectedNotes) { "Expected notes: $expectedNotes, but got: ${scaleEMinor.notes}" }

        val scaleGMinor = Scale(Note.fromString("G4"), ScaleTypes.MINOR)
        assertEquals(8, scaleGMinor.notes.size)
        expectedNotes = listOf("G4", "A4", "Bb4", "C5", "D5", "Eb5", "F5", "G5").map(Note::fromString)
    }
}