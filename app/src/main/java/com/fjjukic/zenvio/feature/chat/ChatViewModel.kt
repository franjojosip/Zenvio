package com.fjjukic.zenvio.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.chat.model.ChatEffect
import com.fjjukic.zenvio.feature.chat.model.ChatError
import com.fjjukic.zenvio.feature.chat.model.ChatIntent
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import com.fjjukic.zenvio.feature.chat.model.ChatRole
import com.fjjukic.zenvio.feature.chat.model.ChatStateUi
import com.fjjukic.zenvio.feature.chat.model.GetAIResponseUseCase
import com.fjjukic.zenvio.feature.chat.model.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getAIResponseUseCase: GetAIResponseUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect = _effect.asSharedFlow()

    private val _uiState = MutableStateFlow(ChatStateUi())
    val uiState = _uiState.asStateFlow()

    private var sendMessageJob: Job? = null

    init {
        _uiState.update {
            it.copy(
                messages = listOf(
                    ChatMessage.Standard(
                        role = ChatRole.ASSISTANT,
                        content = UiText.StringResource(R.string.assistant_intro_message)
                    )
                )
            )
        }
    }

    fun onIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.InputChanged -> handleInputUpdate(intent.text)
            ChatIntent.SendMessage -> handleSendMessageAction()
            ChatIntent.ClearChat -> handleClearChatAction()
            ChatIntent.ExportChat -> handleExportAction()
            ChatIntent.Search -> handleSearchAction()
        }
    }

    private fun handleSearchAction() {
        viewModelScope.launch {
            _effect.emit(ChatEffect.ShowToast(R.string.action_not_implemented))
        }
    }

    private fun handleInputUpdate(text: String) {
        _uiState.update { it.copy(input = text) }
    }

    private fun handleClearChatAction() {
        _uiState.update { it.copy(messages = emptyList()) }
    }

    private fun handleExportAction() {
        viewModelScope.launch {
            _effect.emit(ChatEffect.ShowToast(R.string.action_not_implemented))
        }
    }

    private fun handleSendMessageAction() {
        val input = uiState.value.input.trim()
        if (input.isBlank()) return

        val userMessage = ChatMessage.Standard(
            role = ChatRole.USER,
            content = UiText.Dynamic(input)
        )

        sendMessageJob?.cancel()

        val latestMessages = uiState.value.messages + userMessage

        _uiState.update {
            it.copy(
                messages = latestMessages,
                input = "",
                isLoading = true
            )
        }

        sendMessageJob = viewModelScope.launch {
            getAIResponseUseCase(listOf(userMessage))
                .onSuccess { assistantMessage ->
                    _uiState.update {
                        it.copy(
                            messages = latestMessages + assistantMessage,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    if (error is ChatError.CancellationFailure) {
                        return@onFailure
                    }
                    val contentMessage = mapErrorToMessage(error)
                    val assistantMessage = ChatMessage.Standard(
                        role = ChatRole.ASSISTANT,
                        content = contentMessage
                    )

                    _uiState.update {
                        it.copy(
                            messages = latestMessages + assistantMessage,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun mapErrorToMessage(error: Throwable): UiText {
        return error.message?.let {
            UiText.Dynamic(it)
        } ?: run {
            UiText.StringResource(R.string.error_message)
        }
    }
}