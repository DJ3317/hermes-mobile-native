package com.hermes.mobile.presentation.screens.messaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermes.mobile.domain.models.PlatformGateway
import com.hermes.mobile.domain.repositories.MessagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessagingUiState(val platforms: List<PlatformGateway> = emptyList(), val isLoading: Boolean = false)

@HiltViewModel
class MessagingViewModel @Inject constructor(private val repo: MessagingRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MessagingUiState())
    val uiState: StateFlow<MessagingUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { viewModelScope.launch { _uiState.update { it.copy(isLoading = true) }; try { _uiState.update { it.copy(platforms = repo.getPlatforms(), isLoading = false) } } catch (_: Exception) { _uiState.update { it.copy(isLoading = false) } } } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(viewModel: MessagingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("消息网关") }) }) { padding ->
        if (uiState.isLoading) { Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }; return@Scaffold }
        if (uiState.platforms.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) { Text("📡", style = MaterialTheme.typography.displayMedium); Text("暂无消息平台配置", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(uiState.platforms, key = { it.id }) { platform ->
                    Surface(modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) { Text(platform.name, style = MaterialTheme.typography.titleMedium); Text(platform.type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                            if (platform.connected) Text("已连接", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}
