package com.hermes.mobile.presentation.screens.profiles
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.Profile
import com.hermes.mobile.domain.usecases.profiles.GetProfilesUseCase
import com.hermes.mobile.domain.usecases.profiles.CreateProfileUseCase
import com.hermes.mobile.domain.usecases.profiles.DeleteProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfilesUiState(val profiles: List<Profile> = emptyList(), val isLoading: Boolean = false)

@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val getProfilesUseCase: GetProfilesUseCase,
    private val createProfileUseCase: CreateProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfilesUiState())
    val uiState: StateFlow<ProfilesUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(profiles = getProfilesUseCase(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
    fun create(name: String, label: String) { viewModelScope.launch { try { createProfileUseCase(name, label); load() } catch (_: Exception) { } } }
    fun delete(name: String) { viewModelScope.launch { try { deleteProfileUseCase(name); load() } catch (_: Exception) { } } }
}
