package com.hermes.mobile.presentation.screens.review
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.CodeReview
import com.hermes.mobile.domain.usecases.review.GetReviewsUseCase
import com.hermes.mobile.domain.usecases.review.GetReviewDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewUiState(val reviews: List<CodeReview> = emptyList(), val detail: CodeReview? = null, val isLoading: Boolean = false)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReviewDetailUseCase: GetReviewDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(reviews = getReviewsUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
    fun loadDetail(id: String) { viewModelScope.launch { try { _uiState.update { it.copy(detail = getReviewDetailUseCase(id)) } } catch (_: Exception) { } } }
}
