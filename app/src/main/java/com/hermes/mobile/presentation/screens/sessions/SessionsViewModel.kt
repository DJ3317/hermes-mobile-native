package com.hermes.mobile.presentation.screens.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.Session
import com.hermes.mobile.domain.usecases.sessions.GetSessionsUseCase
import com.hermes.mobile.domain.usecases.sessions.SearchSessionsUseCase
import com.hermes.mobile.domain.usecases.sessions.DeleteSessionUseCase
import com.hermes.mobile.domain.usecases.sessions.RenameSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionsUiState(
    val sessions: List<Session> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val getSessionsUseCase: GetSessionsUseCase,
    private val searchSessionsUseCase: SearchSessionsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val renameSessionUseCase: RenameSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionsUiState())
    val uiState: StateFlow<SessionsUiState> = _uiState.asStateFlow()

    init { loadSessions() }

    fun loadSessions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val sessions = getSessionsUseCase()
                _uiState.update { it.copy(sessions = sessions, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun search(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            if (query.isBlank()) { loadSessions(); return@launch }
            try {
                val sessions = searchSessionsUseCase(query)
                _uiState.update { it.copy(sessions = sessions) }
            } catch (_: Exception) { }
        }
    }

    fun deleteSession(id: String) {
        viewModelScope.launch {
            try { deleteSessionUseCase(id); loadSessions() } catch (_: Exception) { }
        }
    }

    fun renameSession(id: String, title: String) {
        viewModelScope.launch {
            try { renameSessionUseCase(id, title); loadSessions() } catch (_: Exception) { }
        }
    }
}
