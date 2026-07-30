package com.hermes.mobile.presentation.screens.agents
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.SubAgent
import com.hermes.mobile.domain.usecases.agents.GetAgentsUseCase
import com.hermes.mobile.domain.usecases.agents.PauseAgentUseCase
import com.hermes.mobile.domain.usecases.agents.ResumeAgentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AgentsUiState(val agents: List<SubAgent> = emptyList(), val isLoading: Boolean = false)

@HiltViewModel
class AgentsViewModel @Inject constructor(
    private val getAgentsUseCase: GetAgentsUseCase,
    private val pauseAgentUseCase: PauseAgentUseCase,
    private val resumeAgentUseCase: ResumeAgentUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AgentsUiState())
    val uiState: StateFlow<AgentsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(agents = getAgentsUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
    fun pause(id: String) { viewModelScope.launch { try { pauseAgentUseCase(id); load() } catch (_: Exception) { } } }
    fun resume(id: String) { viewModelScope.launch { try { resumeAgentUseCase(id); load() } catch (_: Exception) { } } }
}
