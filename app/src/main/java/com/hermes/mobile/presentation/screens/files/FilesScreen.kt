package com.hermes.mobile.presentation.screens.files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(viewModel: FilesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadFiles("/") }
    Scaffold(topBar = { TopAppBar(title = { Text(uiState.currentPath) }) }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(uiState.files, key = { it.path }) { file ->
                Surface(onClick = { if (file.isDirectory) viewModel.loadFiles(file.path) else viewModel.loadContent(file.path) }, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Text(if (file.isDirectory) "📁 " else "📄 ", style = MaterialTheme.typography.titleMedium)
                        Column(Modifier.weight(1f)) { Text(file.name, style = MaterialTheme.typography.bodyLarge) }
                    }
                }
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}
