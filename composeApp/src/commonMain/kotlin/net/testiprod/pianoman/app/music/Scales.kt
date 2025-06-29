package net.testiprod.pianoman.app.music

data class ScaleType(val name: String, val intervals: List<Int>)

private const val HALF_STEP = 1
private const val WHOLE_STEP = 2

object ScaleTypes {
    val MAJOR = ScaleType("Major", listOf(WHOLE_STEP, WHOLE_STEP, HALF_STEP, WHOLE_STEP, WHOLE_STEP, WHOLE_STEP, HALF_STEP))
    val MINOR = ScaleType("Natural Minor", listOf(WHOLE_STEP, HALF_STEP, WHOLE_STEP, WHOLE_STEP, HALF_STEP, WHOLE_STEP, WHOLE_STEP))
    val HARMONIC_MINOR = ScaleType("Harmonic Minor", listOf(WHOLE_STEP, HALF_STEP, WHOLE_STEP, WHOLE_STEP, HALF_STEP, WHOLE_STEP + HALF_STEP, HALF_STEP))
    val MELODIC_MINOR_ASCENDING = ScaleType("Melodic Minor Ascending", listOf(WHOLE_STEP, HALF_STEP, WHOLE_STEP, WHOLE_STEP, WHOLE_STEP, WHOLE_STEP, HALF_STEP))
}

data class Scale(val root: Note, val type: ScaleType, val preferSharps: Boolean = true) {
    val notes: List<Note> = generateScaleNotes()

    private fun generateScaleNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        var currentNote = root

        notes.add(currentNote)

        for (interval in type.intervals) {
            currentNote = currentNote.transpose(interval, preferSharps)
            notes.add(currentNote)
        }

        return notes
    }
}

