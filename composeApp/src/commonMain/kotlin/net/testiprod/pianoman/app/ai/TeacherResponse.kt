package net.testiprod.pianoman.app.ai

import net.testiprod.pianoman.app.music.Note

data class TeacherResponse(
    val question: String,
    val answerNotes: List<Note>?,
    val answerDescription: String,
    val additionalContext: String?
)