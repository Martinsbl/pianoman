package net.testiprod.pianoman.app.ai

import dev.langchain4j.service.SystemMessage

interface AiTeacher {
    @SystemMessage(
        """
        You are a music teacher specialized in teaching piano chords.
        Your task is to generate a chord question and provide the correct answer.
        The question should be about identifying a chord based on its notes.
        The answer should be a list of strings representing the chord notes.
    """
    )
    fun teach(prompt: String): TeacherResponse
}