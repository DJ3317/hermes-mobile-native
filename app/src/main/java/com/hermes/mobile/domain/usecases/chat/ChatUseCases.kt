package com.hermes.mobile.domain.usecases.chat

import com.hermes.mobile.domain.models.StreamEvent
import com.hermes.mobile.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(sessionId: String, content: String): Flow<StreamEvent> =
        chatRepository.streamMessage(sessionId, content)
}

class StopStreamingUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(sessionId: String) =
        chatRepository.stopStreaming(sessionId)
}

class GetMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(sessionId: String) =
        chatRepository.getMessages(sessionId)
}
