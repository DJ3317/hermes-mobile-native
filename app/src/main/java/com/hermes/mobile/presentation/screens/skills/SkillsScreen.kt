package com.hermes.mobile.presentation.screens.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.Skill
import com.hermes.mobile.domain.repositories.SkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SkillsUiState(
    val skills: List<Skill> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class SkillsViewModel @Inject constructor(
    private val skillRepository: SkillRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SkillsUiState())
    val uiState: StateFlow<SkillsUiState> = _uiState.asStateFlow()

    init { loadSkills() }

    fun loadSkills() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val skills = skillRepository.getSkills()
                _uiState.update { it.copy(skills = skills, isLoading = false) }
            } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } }
        }
    }

    fun toggleSkill(name: String, enabled: Boolean) {
        viewModelScope.launch { try { skillRepository.toggleSkill(name, enabled); loadSkills() } catch (_: Exception) { } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(viewModel: SkillsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = { TopAppBar(title = { Text("技能") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)) }
    ) { padding ->
        if (uiState.isLoading) { Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }; return@Scaffold }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(uiState.skills, key = { it.name }) { skill ->
                Surface(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("🧠 ${skill.name}", style = MaterialTheme.typography.titleMedium)
                            if (skill.description.isNotBlank()) {
                                Text(skill.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Switch(checked = skill.enabled, onCheckedChange = { viewModel.toggleSkill(skill.name, it) })
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}
