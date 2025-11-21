package com.fjjukic.zenvio.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.feature.chat.domain.SendMessageUseCase
import com.fjjukic.zenvio.feature.chat.model.ChatIntent
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChatStateUi())
    val state = _state.asStateFlow()

    fun onIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.InputChanged -> updateInput(intent.text)

            ChatIntent.SendMessage -> send()
            ChatIntent.ClearChat -> clear()
            ChatIntent.ExportChat -> export()
            ChatIntent.LoadInitial -> {}
        }
    }

    private fun updateInput(text: String) {
        _state.update { it.copy(input = text) }
    }

    private fun clear() {
        _state.update { it.copy(messages = emptyList()) }
    }

    private fun export() {
        // Write exported messages to a file
    }

    private fun send() {
        val input = state.value.input
        if (input.isBlank()) return

        // Add user message
        val newMessages = state.value.messages + ChatMessage.User(input)

        _state.update {
            it.copy(
                messages = newMessages + ChatMessage.Loading,
                input = "",
                isLoading = true
            )
        }

        viewModelScope.launch {
            val response = sendMessageUseCase(newMessages)

            _state.update {
                it.copy(
                    isLoading = false,
                    messages = (newMessages + ChatMessage.Assistant(response))
                )
            }
        }
    }

    data class ChatStateUi(
        val messages: List<ChatMessage> = emptyList(),
        val input: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )
}
