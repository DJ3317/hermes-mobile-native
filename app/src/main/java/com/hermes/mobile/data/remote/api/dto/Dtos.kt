package com.hermes.mobile.data.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionDto(
    val id: String,
    val title: String,
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("updated_at") val updatedAt: String = "",
    @SerialName("message_count") val messageCount: Int = 0,
    val archived: Boolean = false,
    val pinned: Boolean = false,
    val profile: String? = null,
    val model: String? = null
)

@Serializable
data class SessionListResponse(
    val sessions: List<SessionDto>,
    val total: Int = 0
)

@Serializable
data class MessageDto(
    val id: String,
    @SerialName("session_id") val sessionId: String,
    val role: String,
    val content: String,
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("token_count") val tokenCount: Int? = null,
    @SerialName("tool_calls") val toolCalls: List<ToolCallDto>? = null
)

@Serializable
data class ToolCallDto(
    val id: String,
    val name: String,
    val arguments: String = "",
    val status: String = "complete"
)

@Serializable
data class ToolResultDto(
    @SerialName("tool_call_id") val toolCallId: String,
    val output: String = "",
    val error: String? = null
)

@Serializable
data class MessageListResponse(
    val messages: List<MessageDto>
)

@Serializable
data class StatusDto(
    val status: String = "",
    val version: String? = null,
    @SerialName("active_sessions") val activeSessions: Int? = null
)

@Serializable
data class ModelOptionDto(
    val id: String,
    val provider: String,
    val model: String,
    @SerialName("display_name") val displayName: String = "",
    val description: String? = null
)

@Serializable
data class ModelOptionResponse(
    val models: List<ModelOptionDto>
)

@Serializable
data class SkillDto(
    val name: String,
    val description: String = "",
    val enabled: Boolean = false,
    val version: String? = null,
    val source: String? = null
)

@Serializable
data class SkillListResponse(
    val skills: List<SkillDto>
)

@Serializable
data class ProfileDto(
    val name: String,
    val label: String = "",
    val model: String? = null,
    val provider: String? = null,
    @SerialName("is_active") val isActive: Boolean = false
)

@Serializable
data class ProfileListResponse(
    val profiles: List<ProfileDto>
)

@Serializable
data class GatewayDto(
    val id: String,
    val name: String = "",
    val type: String = "",
    val connected: Boolean = false,
    val enabled: Boolean = false
)

@Serializable
data class GatewayListResponse(
    val platforms: List<GatewayDto>
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String = "",
    val message: String? = null
)

@Serializable
data class WsTicketResponse(
    val ticket: String = ""
)

@Serializable
data class ModelSetRequest(
    val scope: String = "global",
    val provider: String,
    val model: String
)

@Serializable
data class SkillToggleRequest(
    val name: String,
    val enabled: Boolean
)

@Serializable
data class ProfileCreateRequest(
    val name: String,
    val label: String
)

@Serializable
data class CronTaskDto(
    val id: String,
    val name: String = "",
    val schedule: String = "",
    val prompt: String = "",
    val enabled: Boolean = false,
    @SerialName("last_run") val lastRun: String? = null,
    @SerialName("next_run") val nextRun: String? = null
)

@Serializable
data class CronTaskListResponse(
    val jobs: List<CronTaskDto>
)

@Serializable
data class AgentDto(
    val id: String,
    val name: String = "",
    val status: String = "",
    val progress: Float = 0f
)

@Serializable
data class StatusResponse(
    val status: String
)

@Serializable
data class ErrorResponse(
    val detail: String = ""
)
