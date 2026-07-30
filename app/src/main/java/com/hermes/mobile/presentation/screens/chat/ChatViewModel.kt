package com.hermes.mobile.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.*
import com.hermes.mobile.domain.usecases.chat.SendMessageUseCase
import com.hermes.mobile.domain.usecases.chat.StopStreamingUseCase
import com.hermes.mobile.domain.usecases.chat.GetMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isStreaming: Boolean = false,
    val streamingContent: String? = null,
    val inputText: String = "",
    val currentSessionId: String? = null,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val stopStreamingUseCase: StopStreamingUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        val sessionId = _uiState.value.currentSessionId ?: "session-${System.currentTimeMillis()}"
        _uiState.update {
            it.copy(
                messages = it.messages + Message(
                    id = "msg-${System.currentTimeMillis()}",
                    sessionId = sessionId,
                    role = MessageRole.USER,
                    content = text,
                    createdAt = java.time.Instant.now().toString()
                ),
                currentSessionId = sessionId,
                isStreaming = true,
                streamingContent = "",
                inputText = ""
            )
        }

        viewModelScope.launch {
            try {
                sendMessageUseCase(sessionId, text).collect { event ->
                    when (event) {
                        is StreamEvent.MessageDelta -> {
                            _uiState.update { it.copy(streamingContent = (it.streamingContent ?: "") + event.delta) }
                        }
                        is StreamEvent.MessageComplete -> {
                            val content = _uiState.value.streamingContent ?: ""
                            _uiState.update {
                                it.copy(
                                    messages = it.messages + Message(
                                        id = "msg-${System.currentTimeMillis()}",
                                        sessionId = sessionId,
                                        role = MessageRole.ASSISTANT,
                                        content = content,
                                        createdAt = java.time.Instant.now().toString()
                                    ),
                                    streamingContent = null,
                                    isStreaming = false
                                )
                            }
                        }
                        is StreamEvent.Error -> {
                            _uiState.update { it.copy(error = event.message, isStreaming = false, streamingContent = null) }
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "请求失败", isStreaming = false) }
            }
        }
    }

    fun stopStreaming() {
        val sessionId = _uiState.value.currentSessionId ?: return
        viewModelScope.launch { stopStreamingUseCase(sessionId) }
    }

    fun loadMessages(sessionId: String) {
        viewModelScope.launch {
            try {
                val messages = getMessagesUseCase(sessionId)
                _uiState.update { it.copy(messages = messages, currentSessionId = sessionId) }
            } catch (_: Exception) { }
        }
    }

    fun setInputText(text: String) { _uiState.update { it.copy(inputText = text) } }
    fun clearChat() { _uiState.update { ChatUiState() } }
    fun clearError() { _uiState.update { it.copy(error = null) } }
}
