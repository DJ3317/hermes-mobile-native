package com.hermes.mobile.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GatewayCard(name: String, type: String, connected: Boolean, enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(name, style = MaterialTheme.typography.titleMedium); Text(type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            if (connected) Text("已连接", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
            Switch(checked = enabled, onCheckedChange = onToggle)
        }
    }
}

@Composable
fun ArtifactCard(name: String, type: String, size: Long, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(when (type) { "image" -> "🖼️"; "code" -> "📄"; "pdf" -> "📕"; else -> "📦" }, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(name, style = MaterialTheme.typography.titleMedium); Text(formatSize(size), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
fun CronTaskCard(name: String, schedule: String, enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(name, style = MaterialTheme.typography.titleMedium); Text(schedule, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Switch(checked = enabled, onCheckedChange = onToggle)
        }
    }
}

@Composable
fun AgentCard(name: String, status: String, progress: Float) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Surface(color = when (status) { "running" -> MaterialTheme.colorScheme.primary; "paused" -> MaterialTheme.colorScheme.tertiary; else -> MaterialTheme.colorScheme.surfaceVariant }, shape = MaterialTheme.shapes.small) {
                    Text(status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            if (status == "running") { Spacer(Modifier.height(8.dp)); LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth()) }
        }
    }
}

@Composable
fun SkillCard(name: String, description: String, enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text("🧠 $name", style = MaterialTheme.typography.titleMedium); if (description.isNotBlank()) Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Switch(checked = enabled, onCheckedChange = onToggle)
        }
    }
}

private fun formatSize(bytes: Long): String = when {
    bytes >= 1_000_000 -> "${bytes / 1_000_000}MB"
    bytes >= 1_000 -> "${bytes / 1_000}KB"
    else -> "${bytes}B"
}
