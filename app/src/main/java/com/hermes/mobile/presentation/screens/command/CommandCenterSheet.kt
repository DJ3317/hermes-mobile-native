package com.hermes.mobile.presentation.screens.command

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
fun CommandCenterSheet(viewModel: CommandViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    if (!uiState.isVisible) return

    ModalBottomSheet(onDismissRequest = { viewModel.hide() }) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { viewModel.search(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("搜索命令...") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                items(uiState.results, key = { it.id }) { item ->
                    Text("${item.icon} ${item.label}", modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
                }
            }
        }
    }
}
