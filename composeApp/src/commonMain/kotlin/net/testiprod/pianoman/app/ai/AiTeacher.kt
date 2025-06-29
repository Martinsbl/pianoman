package net.testiprod.pianoman.app.ai

import dev.langchain4j.service.Result
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.V


interface AiTeacher {

    @SystemMessage("{{systemMessage}}")
    @UserMessage("{{prompt}}")
    fun teach(@V("systemMessage") systemMessage: String?, @V("prompt") prompt: String): Result<TeacherResponse>
}