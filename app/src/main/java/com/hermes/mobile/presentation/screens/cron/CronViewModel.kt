package com.hermes.mobile.presentation.screens.cron
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.CronTask
import com.hermes.mobile.domain.usecases.cron.GetCronTasksUseCase
import com.hermes.mobile.domain.usecases.cron.ToggleCronTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CronUiState(val tasks: List<CronTask> = emptyList(), val isLoading: Boolean = false)

@HiltViewModel
class CronViewModel @Inject constructor(
    private val getCronTasksUseCase: GetCronTasksUseCase,
    private val toggleCronTaskUseCase: ToggleCronTaskUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CronUiState())
    val uiState: StateFlow<CronUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(tasks = getCronTasksUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
    fun toggle(id: String, enabled: Boolean) { viewModelScope.launch { try { toggleCronTaskUseCase(id, enabled); load() } catch (_: Exception) { } } }
}
