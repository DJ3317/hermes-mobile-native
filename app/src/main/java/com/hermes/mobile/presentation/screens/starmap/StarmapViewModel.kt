package com.hermes.mobile.presentation.screens.starmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.StarmapNode
import com.hermes.mobile.domain.usecases.starmap.GetStarmapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StarmapUiState(val nodes: List<StarmapNode> = emptyList(), val isLoading: Boolean = false)

@HiltViewModel
class StarmapViewModel @Inject constructor(
    private val getStarmapUseCase: GetStarmapUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(StarmapUiState())
    val uiState: StateFlow<StarmapUiState> = _uiState.asStateFlow()

    init { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(nodes = getStarmapUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
}
