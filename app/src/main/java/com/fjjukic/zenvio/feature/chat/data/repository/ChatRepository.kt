package com.fjjukic.zenvio.feature.chat.data.repository

import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.api.ChatRequest
import com.fjjukic.zenvio.core.api.ChatRoleMessage
import com.fjjukic.zenvio.core.api.OpenAIApi
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import com.fjjukic.zenvio.feature.chat.model.ChatRole
import com.fjjukic.zenvio.feature.chat.model.UiText
import javax.inject.Inject

interface ChatRepository {
    suspend fun sendMessage(messages: List<ChatMessage>): UiText
}

class ChatRepositoryImpl @Inject constructor(
    private val api: OpenAIApi
) : ChatRepository {
    override suspend fun sendMessage(messages: List<ChatMessage>): UiText {
        val formatted = messages.mapNotNull { message ->
            if (message is ChatMessage.Standard && message.content is UiText.Dynamic) {
                val roleName = when (message.role) {
                    ChatRole.USER -> "user"
                    ChatRole.ASSISTANT -> "assistant"
                }
                ChatRoleMessage(roleName, message.content.value)
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

        val responseMessage = response.choices.firstOrNull()?.message?.content

        return if (responseMessage != null) {
            UiText.Dynamic(responseMessage)
        } else {
            UiText.StringResource(R.string.chat_api_no_response)
        }
    }
}