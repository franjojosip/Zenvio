package com.fjjukic.zenvio.feature.chat.model

import android.util.Log
import com.fjjukic.zenvio.feature.chat.data.repository.ChatRepository
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class GetAIResponseUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(messages: List<ChatMessage>): Result<ChatMessage> {
        return try {
            val receivedContent = repository.sendMessage(messages)

            Result.success(
                ChatMessage.Standard(
                    role = ChatRole.ASSISTANT,
                    content = receivedContent
                )
            )

        } catch (e: Exception) {
            Log.e("GetAIResponseUseCase", "Network request failed", e)

            val error = when (e) {

                // No internet OR DNS resolution failed
                is UnknownHostException -> ChatError.NoInternet()

                // Timeout, SSL, connection drop
                is java.net.SocketTimeoutException,
                is javax.net.ssl.SSLException,
                is java.net.ConnectException -> ChatError.NetworkFailure()

                // On coroutine canceled
                is CancellationException -> ChatError.CancellationFailure()

                // Fallback
                else -> ChatError.UnknownError()
            }

            Result.failure(error)
        }
    }
}