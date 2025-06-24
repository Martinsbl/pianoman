package net.testiprod.midi.client

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.isSuccess

sealed class NetworkResult<out S : Any, out E : Any> {
    data class Success<out S : Any>(
        val data: S,
    ) : NetworkResult<S, Nothing>()

    data class Error<out E : Any>(
        val code: Int,
        val simpleErrorMessage: String,
        val advancedErrorMessage: String? = null,
        val errorData: E? = null,
    ) : NetworkResult<Nothing, E>()

    companion object {
        suspend inline fun <reified T : Any, reified E : Any> doNetworkRequest(
            crossinline block: suspend () -> HttpResponse
        ): NetworkResult<T, E> {
            return try {
                val response: HttpResponse = block.invoke()
                if (response.status.isSuccess()) {
                    val data = response.body<T>()
                    Success(data)
                } else {
                    val errorData = response.body<E>()
                    Error(
                        code = response.status.value,
                        simpleErrorMessage = getSimpleErrorMessage(response.status.value),
                        advancedErrorMessage = "${response.status} at ${response.request.method.value} ${response.request.url.encodedPath}",
                        errorData = errorData,
                    )
                }
            } catch (e: Exception) {
                Error(
                    code = 500,
                    simpleErrorMessage = e.message ?: "An unexpected error occurred",
                    advancedErrorMessage = e.stackTraceToString().take(2000),
                )
            }
        }

        fun getSimpleErrorMessage(statusCode: Int): String = when (statusCode) {
            // 4xx Client Errors
            400 -> "Invalid request - please check your input"
            401 -> "Authentication required - please log in"
            403 -> "Access denied - you don't have permission to access this"
            404 -> "The requested resource was not found"
            405 -> "This operation is not allowed"
            408 -> "The request timed out - please try again"
            429 -> "Too many requests - please wait and try again later"

            // 5xx Server Errors
            500 -> "An unexpected server error occurred - please try again later"
            501 -> "This feature is not supported yet"
            502 -> "Server is temporarily unavailable - please try again later"
            503 -> "Service unavailable - please try again later"
            504 -> "Server timeout - please try again later"

            // Generic fallbacks
            in 400..499 -> "Something was wrong with the request - please check your input"
            in 500..599 -> "A server error occurred - please try again later"

            else -> "An unexpected error occurred"
        }
    }

}
