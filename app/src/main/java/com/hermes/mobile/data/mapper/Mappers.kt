package com.hermes.mobile.data.mapper

import com.hermes.mobile.data.remote.api.dto.*
import com.hermes.mobile.domain.models.*
import java.time.Instant

fun SessionDto.toDomain() = Session(
    id = id, title = title, createdAt = Instant.parse(createdAt), updatedAt = Instant.parse(updatedAt),
    messageCount = messageCount, archived = archived, pinned = pinned,
    profile = profile, model = model
)

fun MessageDto.toDomain() = Message(
    id = id, sessionId = sessionId,
    role = try { MessageRole.valueOf(role.uppercase()) } catch (_: Exception) { MessageRole.USER },
    content = content, createdAt = Instant.parse(createdAt), tokenCount = tokenCount,
    toolCalls = toolCalls?.map { it.toDomain() }
)

fun ToolCallDto.toDomain() = ToolCall(
    id = id, name = name, arguments = arguments,
    status = try { ToolCallStatus.valueOf(status.uppercase()) } catch (_: Exception) { ToolCallStatus.COMPLETE }
)

fun ModelOptionDto.toDomain() = ModelOption(
    id = id, provider = provider, model = model,
    displayName = displayName, description = description
)

fun SkillDto.toDomain() = Skill(
    name = name, description = description,
    enabled = enabled, version = version, source = source
)

fun ProfileDto.toDomain() = Profile(
    name = name, label = label, model = model,
    provider = provider, isActive = isActive
)

fun GatewayDto.toDomain() = PlatformGateway(
    id = id, name = name, type = type,
    connected = connected, enabled = enabled
)

fun CronTaskDto.toDomain() = CronTask(
    id = id, name = name, schedule = schedule, prompt = prompt,
    enabled = enabled, lastRun = lastRun, nextRun = nextRun
)

fun AgentDto.toDomain() = SubAgent(
    id = id, name = name, status = status, progress = progress
)
