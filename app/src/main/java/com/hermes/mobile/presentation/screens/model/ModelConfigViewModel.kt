package com.hermes.mobile.presentation.screens.model
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.ModelOption
import com.hermes.mobile.domain.usecases.settings.GetModelsUseCase
import com.hermes.mobile.domain.usecases.settings.SetModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModelConfigUiState(val models: List<ModelOption> = emptyList(), val selectedModel: String? = null, val isLoading: Boolean = false)

@HiltViewModel
class ModelConfigViewModel @Inject constructor(
    private val getModelsUseCase: GetModelsUseCase,
    private val setModelUseCase: SetModelUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ModelConfigUiState())
    val uiState: StateFlow<ModelConfigUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(models = getModelsUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
    fun select(provider: String, model: String) { viewModelScope.launch { try { setModelUseCase(provider, model); _uiState.update { it.copy(selectedModel = model) } } catch (_: Exception) { } } }
}
