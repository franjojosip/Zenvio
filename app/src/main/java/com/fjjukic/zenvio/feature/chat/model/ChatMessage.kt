package com.fjjukic.zenvio.feature.chat.model

import java.util.UUID

sealed class ChatMessage(
    // Adding a unique ID is crucial for LazyColumn performance.
    // It helps Compose efficiently track items during recomposition.
    val id: String = UUID.randomUUID().toString()
) {
    data class Standard(
        val role: ChatRole,
        val content: String
    ) : ChatMessage()

    data class Loading(
        val placeholder: Boolean = true
    ) : ChatMessage()
}