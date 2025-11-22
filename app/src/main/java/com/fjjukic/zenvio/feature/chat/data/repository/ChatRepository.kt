package com.fjjukic.zenvio.feature.chat.data.repository

import com.fjjukic.zenvio.core.api.ChatRequest
import com.fjjukic.zenvio.core.api.ChatRoleMessage
import com.fjjukic.zenvio.core.api.OpenAIApi
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import com.fjjukic.zenvio.feature.chat.model.ChatRole
import javax.inject.Inject

interface ChatRepository {
    suspend fun sendMessage(messages: List<ChatMessage>): String
}

class ChatRepositoryImpl @Inject constructor(
    private val api: OpenAIApi
) : ChatRepository {
    override suspend fun sendMessage(messages: List<ChatMessage>): String {
        val formatted = messages.mapNotNull { message ->
            if (message is ChatMessage.Standard) {
                val roleName = when (message.role) {
                    ChatRole.USER -> "user"
                    ChatRole.ASSISTANT -> "assistant"
                }
                ChatRoleMessage(roleName, message.content)
            } else {
                null
            }
        }

        val response = api.chatCompletion(
            ChatRequest(
                model = "gpt-4o-mini",
                messages = formatted
            )
        )

        return response.choices.firstOrNull()?.message?.content
            ?: "Sorry, I couldn't provide a response."
    }
}