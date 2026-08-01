package com.hermes.mobile.presentation.screens.logs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.hermes.mobile.data.local.LogLevel
import com.hermes.mobile.data.local.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class LogsUiState(val logs: List<com.hermes.mobile.data.local.LogEntry> = emptyList())

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val logger: Logger
) : ViewModel() {
    private val _uiState = MutableStateFlow(LogsUiState())
    val uiState: StateFlow<LogsUiState> = _uiState.asStateFlow()

    fun refresh() {
        _uiState.update { it.copy(logs = logger.logs) }
    }

    fun clear() {
        logger.clear()
        refresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(viewModel: LogsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.refresh() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("调试日志") },
                actions = {
                    TextButton(onClick = { viewModel.clear() }) { Text("清空") }
                }
            )
        }
    ) { padding ->
        if (uiState.logs.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("暂无日志", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            return@Scaffold
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(uiState.logs.asReversed(), key = { it.id }) { entry ->
                val color = when (entry.level) {
                    LogLevel.ERROR -> Color(0xFFFF453A)
                    LogLevel.WARN -> Color(0xFFFF9F0A)
                    LogLevel.INFO -> MaterialTheme.colorScheme.onSurfaceVariant
                    LogLevel.DEBUG -> MaterialTheme.colorScheme.outline
                }
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text(
                        "[${entry.level}] ${entry.timestamp.toLocalTimeString()} ${entry.tag}",
                        style = MaterialTheme.typography.labelSmall,
                        color = color,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        entry.message + (entry.throwable?.let { "\n  → $it" } ?: ""),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

private fun java.time.Instant.toLocalTimeString(): String {
    val t = atZone(java.time.ZoneId.systemDefault()).toLocalTime()
    return "${t.hour.toString().padStart(2, '0')}:${t.minute.toString().padStart(2, '0')}:${t.second.toString().padStart(2, '0')}"
}
