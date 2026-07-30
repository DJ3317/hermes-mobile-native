package com.hermes.mobile.presentation.screens.files
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.FileEntry
import com.hermes.mobile.domain.usecases.files.ListFilesUseCase
import com.hermes.mobile.domain.usecases.files.GetFileContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilesUiState(val files: List<FileEntry> = emptyList(), val currentPath: String = "/", val content: String? = null, val isLoading: Boolean = false)

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val listFilesUseCase: ListFilesUseCase,
    private val getFileContentUseCase: GetFileContentUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FilesUiState())
    val uiState: StateFlow<FilesUiState> = _uiState.asStateFlow()
    fun loadFiles(path: String) { viewModelScope.launch { _uiState.update { it.copy(isLoading = true, currentPath = path) }; try { _uiState.update { it.copy(files = listFilesUseCase(path), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
    fun loadContent(path: String) { viewModelScope.launch { try { _uiState.update { it.copy(content = getFileContentUseCase(path)) } } catch (_: Exception) { } } }
}
