package com.hermes.mobile.presentation.screens.artifacts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.Artifact
import com.hermes.mobile.domain.usecases.artifacts.GetArtifactsUseCase
import com.hermes.mobile.domain.usecases.artifacts.DeleteArtifactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ArtifactsUiState(val artifacts: List<Artifact> = emptyList(), val isLoading: Boolean = false)

@HiltViewModel
class ArtifactsViewModel @Inject constructor(
    private val getArtifactsUseCase: GetArtifactsUseCase,
    private val deleteArtifactUseCase: DeleteArtifactUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArtifactsUiState())
    val uiState: StateFlow<ArtifactsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(artifacts = getArtifactsUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
    fun delete(id: String) { viewModelScope.launch { try { deleteArtifactUseCase(id); load() } catch (_: Exception) { } } }
}
