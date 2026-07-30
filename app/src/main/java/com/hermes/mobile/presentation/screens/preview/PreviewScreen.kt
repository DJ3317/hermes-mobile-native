package com.hermes.mobile.presentation.screens.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(viewModel: PreviewViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.load(uiState.url) }
    Scaffold(topBar = { TopAppBar(title = { Text("预览") }) }) { padding ->
        if (uiState.isLoading) { Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }; return@Scaffold }
        Text(uiState.content ?: "暂无内容", modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp))
    }
}
