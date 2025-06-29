package net.testiprod.pianoman.app.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.testiprod.pianoman.app.ai.TeacherResponse
import net.testiprod.pianoman.app.ui.mock.teacherResponse

@Composable
fun AiView(
    aiResponseState: UiState<TeacherResponse>,
    onPromptClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var prompt: String by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        UiStateView(aiResponseState) { response ->
            TeacherResponseView(response)
        }
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Override prompt") },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                onPromptClick(prompt)
            }) {
            Text("Get quiz")
        }
    }
}

@Composable
fun TeacherResponseView(
    teacherResponse: TeacherResponse,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = teacherResponse.question,
            modifier = Modifier.fillMaxWidth(),
        )
        teacherResponse.answerNotes?.let {
            Text(
                text = it.joinToString(", "),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Text(
            text = teacherResponse.answerDescription,
            modifier = Modifier.fillMaxWidth(),
        )
        teacherResponse.additionalContext?.let {
            Text(
                text = it,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Scaffold {
        AiView(
            UiState.Success(teacherResponse),
            onPromptClick = { _ -> },
        )
    }
}