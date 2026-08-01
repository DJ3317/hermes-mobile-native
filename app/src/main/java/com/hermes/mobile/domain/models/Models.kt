package com.hermes.mobile.domain.models

import java.time.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object InstantSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}

@Serializable
data class Session(
    val id: String,
    val title: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant = Instant.now(),
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now(),
    val messageCount: Int = 0,
    val archived: Boolean = false,
    val pinned: Boolean = false,
    val profile: String? = null,
    val model: String? = null
)

@Serializable
data class Message(
    val id: String,
    val sessionId: String,
    val role: MessageRole,
    val content: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant = Instant.now(),
    val tokenCount: Int? = null,
    val toolCalls: List<ToolCall>? = null,
    val toolResults: List<ToolResult>? = null
)

enum class MessageRole { USER, ASSISTANT, SYSTEM, TOOL }

@Serializable
data class ToolCall(
    val id: String,
    val name: String,
    val arguments: String,
    val status: ToolCallStatus
)

enum class ToolCallStatus { RUNNING, COMPLETE, ERROR }

@Serializable
data class ToolResult(
    val toolCallId: String,
    val output: String,
    val error: String? = null
)

sealed class StreamEvent {
    data class MessageStart(val sessionId: String, val messageId: String? = null) : StreamEvent()
    data class MessageDelta(val sessionId: String, val messageId: String?, val delta: String) : StreamEvent()
    data class MessageComplete(val sessionId: String, val messageId: String?) : StreamEvent()
    data class ThinkingDelta(val sessionId: String, val delta: String) : StreamEvent()
    data class ToolStart(val sessionId: String, val toolCall: ToolCall) : StreamEvent()
    data class ToolProgress(val sessionId: String, val toolCall: ToolCall) : StreamEvent()
    data class ToolComplete(val sessionId: String, val toolCall: ToolCall) : StreamEvent()
    data class Error(val sessionId: String, val message: String) : StreamEvent()
    data class StatusUpdate(val sessionId: String, val status: String) : StreamEvent()
}

@Serializable
data class ModelOption(
    val id: String,
    val provider: String,
    val model: String,
    val displayName: String,
    val description: String? = null
)

@Serializable
data class Provider(
    val id: String,
    val name: String,
    val type: String,
    val configured: Boolean = false,
    val models: List<ModelOption> = emptyList()
)

@Serializable
data class Skill(
    val name: String,
    val description: String,
    val enabled: Boolean = false,
    val version: String? = null,
    val source: String? = null
)

@Serializable
data class Profile(
    val name: String,
    val label: String,
    val model: String? = null,
    val provider: String? = null,
    val soul: String? = null,
    val isActive: Boolean = false
)

@Serializable
data class PlatformGateway(
    val id: String,
    val name: String,
    val type: String,
    val connected: Boolean = false,
    val enabled: Boolean = false
)

@Serializable
data class Artifact(
    val id: String,
    val name: String,
    val type: String,
    val size: Long = 0,
    val createdAt: String? = null,
    val url: String? = null
)

@Serializable
data class CronTask(
    val id: String,
    val name: String,
    val schedule: String,
    val prompt: String,
    val enabled: Boolean = false,
    val lastRun: String? = null,
    val nextRun: String? = null
)

@Serializable
data class SubAgent(
    val id: String,
    val name: String,
    val status: String,
    val progress: Float = 0f,
    val createdAt: String? = null
)

@Serializable
data class StarmapNode(
    val id: String,
    val label: String,
    val type: String,
    val x: Float = 0f,
    val y: Float = 0f
)

@Serializable
data class StarmapEdge(
    val sourceId: String,
    val targetId: String,
    val label: String? = null
)

@Serializable
data class Project(
    val id: String,
    val name: String,
    val path: String? = null
)

@Serializable
data class FileEntry(
    val name: String,
    val path: String,
    val isDirectory: Boolean = false,
    val size: Long = 0
)

@Serializable
data class CodeReview(
    val id: String,
    val title: String,
    val status: String,
    val files: List<DiffFile> = emptyList()
)

@Serializable
data class DiffFile(
    val fileName: String,
    val hunks: List<DiffHunk> = emptyList()
)

@Serializable
data class DiffHunk(
    val header: String,
    val lines: List<DiffLine> = emptyList()
)

@Serializable
data class DiffLine(
    val type: DiffLineType,
    val content: String,
    val oldLineNo: Int? = null,
    val newLineNo: Int? = null
)

enum class DiffLineType { ADDED, REMOVED, CONTEXT }

@Serializable
data class TerminalSession(
    val id: String,
    val name: String = "Terminal",
    val lines: List<String> = emptyList()
)

@Serializable
data class PreviewContent(
    val url: String? = null,
    val content: String? = null,
    val type: String = "text"
)

@Serializable
data class BackendConfig(
    val host: String = "",
    val token: String? = null
)

@Serializable
data class McpServer(
    val name: String,
    val enabled: Boolean = false,
    val description: String? = null
)

@Serializable
data class ToolConfig(
    val name: String,
    val enabled: Boolean = false
)

@Serializable
data class SearchResult(
    val sessionId: String,
    val title: String,
    val snippet: String? = null,
    val score: Float = 0f
)

@Serializable
data class AppSettings(
    val themeMode: String = "system",
    val language: String = "zh",
    val backendHost: String = "",
    val token: String? = null
)

@Serializable
data class CommandItem(
    val id: String,
    val label: String,
    val icon: String = "",
    val category: String = "general"
)

@Serializable
data class CronRunHistory(
    val id: String,
    val taskId: String,
    val status: String,
    val startedAt: String? = null,
    val completedAt: String? = null
)
