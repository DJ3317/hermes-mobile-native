package com.hermes.mobile.presentation.screens.terminal
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.TerminalSession
import com.hermes.mobile.domain.usecases.terminal.GetTerminalSessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class TerminalUiState(val sessions: List<TerminalSession> = emptyList(), val activeSessionId: String? = null)
@HiltViewModel
class TerminalViewModel @Inject constructor(private val getTerminalSessionsUseCase: GetTerminalSessionsUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(TerminalUiState())
    val uiState: StateFlow<TerminalUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { try { _uiState.update { it.copy(sessions = getTerminalSessionsUseCase()) } } catch (_: Exception) { } } }
}
