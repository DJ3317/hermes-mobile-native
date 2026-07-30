package com.hermes.mobile.presentation.screens.model

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelConfigScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("模型配置") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🤖", style = MaterialTheme.typography.displayMedium)
                Text("模型配置", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
