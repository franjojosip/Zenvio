package com.fjjukic.zenvio.core.data.repository

import com.fjjukic.zenvio.core.api.ChatRequest
import com.fjjukic.zenvio.core.api.ChatRoleMessage
import com.fjjukic.zenvio.core.api.OpenAIApi
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import javax.inject.Inject

interface ChatRepository {
    suspend fun sendMessage(messages: List<ChatMessage>): String
}

class ChatRepositoryImpl @Inject constructor(
    private val api: OpenAIApi
) : ChatRepository {

    override suspend fun sendMessage(messages: List<ChatMessage>): String {
        val formatted = messages.mapNotNull {
            when (it) {
                is ChatMessage.User -> ChatRoleMessage("user", it.text)
                is ChatMessage.Assistant -> ChatRoleMessage("assistant", it.text)
                else -> null
            }
        }

        val response = api.chatCompletion(
            ChatRequest(
                model = "gpt-4o-mini",
                messages = formatted
            )
        )

        return response.choices.first().message.content
    }
}