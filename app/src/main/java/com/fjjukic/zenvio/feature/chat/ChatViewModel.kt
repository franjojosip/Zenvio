package com.fjjukic.zenvio.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.feature.chat.model.ChatIntent
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import com.fjjukic.zenvio.feature.chat.model.ChatRole
import com.fjjukic.zenvio.feature.chat.model.ChatStateUi
import com.fjjukic.zenvio.feature.chat.model.GetAIResponseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getAIResponseUseCase: GetAIResponseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatStateUi())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                messages = listOf(
                    ChatMessage.Standard(
                        role = ChatRole.ASSISTANT,
                        content = "Hello there! I'm Mindy, your mental wellness companion. How can I assist you today?"
                    )
                )
            )
        }
    }

    fun onIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.InputChanged -> updateInput(intent.text)
            ChatIntent.SendMessage -> send()
            ChatIntent.ClearChat -> clear()
            ChatIntent.ExportChat -> export()
            ChatIntent.LoadInitial -> {}
            ChatIntent.Search -> {}
        }
    }

    private fun updateInput(text: String) {
        _uiState.update { it.copy(input = text) }
    }

    private fun clear() {
        _uiState.update { it.copy(messages = emptyList()) }
    }

    private fun export() {
        // Write exported messages to a file
    }

    private fun send() {
        val input = uiState.value.input
        if (input.isBlank()) return

        val userMessage = ChatMessage.Standard(role = ChatRole.USER, content = input)
        val messagesBeforeAI = uiState.value.messages + userMessage

        // Add loading bubble (unique ID!)
        _uiState.update {
            it.copy(
                messages = messagesBeforeAI + ChatMessage.Loading(),
                input = "",
                isLoading = true
            )
        }

        viewModelScope.launch {
            getAIResponseUseCase(messagesBeforeAI)
                .onSuccess { assistantMessage ->
                    // Replace loading with assistant message
                    _uiState.update {
                        it.copy(
                            messages = messagesBeforeAI + assistantMessage,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->

                    val assistantMessage = ChatMessage.Standard(
                        role = ChatRole.ASSISTANT,
                        content = error.message ?: "Something went wrong. Please try again."
                    )

                    _uiState.update {
                        it.copy(
                            messages = messagesBeforeAI + assistantMessage,
                            isLoading = false
                        )
                    }
                }
        }
    }
}