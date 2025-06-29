package net.testiprod.pianoman.app.ui.mock

import net.testiprod.pianoman.app.ai.TeacherResponse
import net.testiprod.pianoman.app.music.Note

val teacherResponse = TeacherResponse(
    "Hello, I am your AI teacher!",
    listOf(Note.fromMidi(44), Note.fromMidi(48), Note.fromMidi(52)),
    "C major chord",
    "The C major chord consists of the notes C, E, and G.",
)