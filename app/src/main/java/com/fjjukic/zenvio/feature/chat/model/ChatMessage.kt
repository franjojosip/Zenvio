package com.fjjukic.zenvio.feature.chat.model

sealed class ChatMessage {
    data class User(val text: String) : ChatMessage()
    data class Assistant(val text: String) : ChatMessage()
    data object Loading : ChatMessage()
}