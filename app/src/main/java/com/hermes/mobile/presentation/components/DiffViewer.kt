package com.hermes.mobile.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.hermes.mobile.domain.models.DiffFile
import com.hermes.mobile.domain.models.DiffLineType

@Composable
fun DiffViewer(diffFiles: List<DiffFile>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(diffFiles) { file ->
            Text(file.fileName, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp, 8.dp))
            file.hunks.forEach { hunk ->
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Text(hunk.header, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.primary)
                    hunk.lines.forEach { line ->
                        val bgColor = when (line.type) {
                            DiffLineType.ADDED -> Color(0x4430D158)
                            DiffLineType.REMOVED -> Color(0x44FF3B30)
                            DiffLineType.CONTEXT -> Color.Transparent
                        }
                        val prefix = when (line.type) { DiffLineType.ADDED -> "+"; DiffLineType.REMOVED -> "-"; else -> " " }
                        Surface(color = bgColor, modifier = Modifier.fillMaxWidth()) {
                            Text(buildAnnotatedString {
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) { append("${line.oldLineNo?.toString()?.padStart(4) ?: "    "}") }
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) { append("${line.newLineNo?.toString()?.padStart(4) ?: "    "}") }
                                append(" $prefix${line.content}")
                            }, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp))
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
