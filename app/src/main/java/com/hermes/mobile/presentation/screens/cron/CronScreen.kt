package com.hermes.mobile.presentation.screens.cron

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("定时任务") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⏰", style = MaterialTheme.typography.displayMedium)
                Text("暂无定时任务", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
