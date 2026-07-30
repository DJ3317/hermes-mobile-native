package com.hermes.mobile.presentation.screens.starmap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarmapScreen(viewModel: StarmapViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("星图") }) }) { padding ->
        if (uiState.isLoading) { Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }; return@Scaffold }
        Canvas(modifier = Modifier.fillMaxSize().padding(padding)) {
            uiState.nodes.forEachIndexed { i, node ->
                drawCircle(Color(0xFF0A84FF), radius = 20f, center = Offset(
                    x = size.width / 2 + (i % 5 - 2) * 100f,
                    y = size.height / 2 + (i / 5 - 1) * 120f
                ))
            }
        }
    }
}
