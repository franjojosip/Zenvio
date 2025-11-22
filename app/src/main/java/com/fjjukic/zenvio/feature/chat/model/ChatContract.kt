package com.fjjukic.zenvio.feature.chat.model

import androidx.annotation.StringRes

data class ChatStateUi(
    val messages: List<ChatMessage> = emptyList(),
    val input: String = "",
    val isLoading: Boolean = false
)

sealed interface ChatEffect {
    data class ShowToast(val messageRes: Int) : ChatEffect
}

sealed interface ChatIntent {
    data class InputChanged(val text: String) : ChatIntent
    data object SendMessage : ChatIntent
    data object ClearChat : ChatIntent
    data object Search : ChatIntent
    data object ExportChat : ChatIntent
}

enum class ChatRole {
    USER,
    ASSISTANT
}

sealed class UiText {
    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText()

    data class Dynamic(val value: String) : UiText()
}