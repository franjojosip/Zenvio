package com.fjjukic.zenvio.feature.chat.domain

import com.fjjukic.zenvio.core.data.repository.ChatRepository
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(messages: List<ChatMessage>): String {
        return repository.sendMessage(messages)
    }
}
