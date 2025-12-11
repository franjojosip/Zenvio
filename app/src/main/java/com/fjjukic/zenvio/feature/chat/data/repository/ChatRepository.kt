package com.fjjukic.zenvio.feature.chat.data.repository

import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.api.ChatRequest
import com.fjjukic.zenvio.core.api.ChatRoleMessage
import com.fjjukic.zenvio.core.api.OpenAIApi
import com.fjjukic.zenvio.core.data.profile.UserProfileManager
import com.fjjukic.zenvio.feature.chat.data.buildPersonalizedPrompt
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import com.fjjukic.zenvio.feature.chat.model.ChatRole
import com.fjjukic.zenvio.feature.chat.model.UiText
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface ChatRepository {
    suspend fun sendMessage(messages: List<ChatMessage>): UiText
}

class ChatRepositoryImpl @Inject constructor(
    private val api: OpenAIApi,
    private val userProfileManager: UserProfileManager
) : ChatRepository {

    override suspend fun sendMessage(messages: List<ChatMessage>): UiText {

        // Load personalized profile
        val profile = userProfileManager.userProfile().first()

        // Build system prompt
        val systemPrompt = buildPersonalizedPrompt(profile)

        // Format the user + assistant messages
        val messageList = messages.mapNotNull { msg ->
            if (msg is ChatMessage.Standard && msg.content is UiText.Dynamic) {
                ChatRoleMessage(
                    when (msg.role) {
                        ChatRole.USER -> "user"
                        ChatRole.ASSISTANT -> "assistant"
                    },
                    msg.content.value
                )
            } else null
        }

        // System + conversation
        val finalMessages = listOf(systemPrompt) + messageList

        // API call
        val response = api.chatCompletion(
            ChatRequest(
                model = "gpt-4o-mini",
                messages = finalMessages
            )
        )

        val content = response.choices.firstOrNull()?.message?.content
        return content?.let { UiText.Dynamic(it) }
            ?: UiText.StringResource(R.string.chat_api_no_response)
    }
}
