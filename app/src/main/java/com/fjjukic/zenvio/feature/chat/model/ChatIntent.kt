package com.fjjukic.zenvio.feature.chat.model

sealed interface ChatIntent {
    data class InputChanged(val text: String) : ChatIntent
    data object SendMessage : ChatIntent
    data object ClearChat : ChatIntent
    data object ExportChat : ChatIntent
    data object LoadInitial : ChatIntent
}