package com.fjjukic.zenvio.feature.chat.model

data class ChatStateUi(
    val messages: List<ChatMessage> = emptyList(),
    val input: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ChatIntent {
    data class InputChanged(val text: String) : ChatIntent
    data object SendMessage : ChatIntent
    data object ClearChat : ChatIntent
    data object Search : ChatIntent
    data object ExportChat : ChatIntent
    data object LoadInitial : ChatIntent
}

enum class ChatRole {
    USER,
    ASSISTANT
}
