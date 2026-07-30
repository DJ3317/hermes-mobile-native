package com.hermes.mobile.presentation.screens.command
import androidx.lifecycle.ViewModel
import com.hermes.mobile.domain.models.CommandItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class CommandUiState(val query: String = "", val results: List<CommandItem> = emptyList(), val isVisible: Boolean = false)

@HiltViewModel
class CommandViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(CommandUiState())
    val uiState: StateFlow<CommandUiState> = _uiState.asStateFlow()

    fun show() { _uiState.update { it.copy(isVisible = true) } }
    fun hide() { _uiState.update { it.copy(isVisible = false, query = "") } }
    fun search(query: String) { _uiState.update { it.copy(query = query) } }
}
