package com.hermes.mobile.presentation.screens.preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.usecases.preview.GetPreviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
data class PreviewUiState(val content: String? = null, val url: String = "", val isLoading: Boolean = false)
@HiltViewModel
class PreviewViewModel @Inject constructor(private val getPreviewUseCase: GetPreviewUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(PreviewUiState())
    val uiState: StateFlow<PreviewUiState> = _uiState.asStateFlow()
    fun load(url: String) { viewModelScope.launch { _uiState.update { it.copy(isLoading = true, url = url) }; try { val p = getPreviewUseCase(url); _uiState.update { it.copy(content = p.content ?: p.url, isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
}
