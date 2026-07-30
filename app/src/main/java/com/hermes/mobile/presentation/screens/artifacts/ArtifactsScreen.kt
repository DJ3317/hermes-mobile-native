package com.hermes.mobile.presentation.screens.artifacts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("产物") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📦", style = MaterialTheme.typography.displayMedium)
                Text("暂无产物", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
