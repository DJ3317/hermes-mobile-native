package com.hermes.mobile.presentation.screens.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Profile 管理") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("👤", style = MaterialTheme.typography.displayMedium)
                Text("Profile 管理", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
