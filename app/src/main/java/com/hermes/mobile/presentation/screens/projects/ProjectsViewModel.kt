package com.hermes.mobile.presentation.screens.projects
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.Project
import com.hermes.mobile.domain.usecases.projects.GetProjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class ProjectsUiState(val projects: List<Project> = emptyList(), val isLoading: Boolean = false)
@HiltViewModel
class ProjectsViewModel @Inject constructor(private val getProjectsUseCase: GetProjectsUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(projects = getProjectsUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
}
