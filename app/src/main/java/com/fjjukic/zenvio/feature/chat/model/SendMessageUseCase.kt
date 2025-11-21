package com.fjjukic.zenvio.feature.chat.model

import com.fjjukic.zenvio.feature.chat.data.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(messages: List<ChatMessage>): String {
        return repository.sendMessage(messages)
    }
}
