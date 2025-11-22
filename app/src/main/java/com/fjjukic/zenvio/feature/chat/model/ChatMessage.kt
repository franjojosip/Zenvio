package com.fjjukic.zenvio.feature.chat.model

import java.util.UUID

sealed class ChatMessage(
    val id: String = UUID.randomUUID().toString()
) {
    data class Standard(
        val role: ChatRole,
        val content: UiText
    ) : ChatMessage()
}