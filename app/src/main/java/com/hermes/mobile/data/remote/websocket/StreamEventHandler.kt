package com.hermes.mobile.data.remote.websocket

import com.hermes.mobile.domain.models.StreamEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton

/** 流式事件分发器 — 从 JSON-RPC 事件帧解析为 StreamEvent */
@Singleton
class StreamEventHandler @Inject constructor() {

    private val json = Json { ignoreUnknownKeys = true }

    /** 解析 JSON-RPC 事件帧，返回对应的 StreamEvent，非事件帧返回 null */
    fun parse(text: String): StreamEvent? {
        return try {
            val root = json.parseToJsonElement(text).jsonObject
            if (root["method"]?.jsonPrimitive?.content != "event") return null

            val params = root["params"]?.jsonObject ?: return null
            val type = params["type"]?.jsonPrimitive?.content ?: return null
            val sessionId = params["session_id"]?.jsonPrimitive?.content ?: ""
            val payload = params["payload"]?.jsonObject

            when (type) {
                "message.start" -> StreamEvent.MessageStart(sessionId)
                "message.delta" -> StreamEvent.MessageDelta(
                    sessionId, payload?.get("message_id")?.jsonPrimitive?.content,
                    payload?.get("text")?.jsonPrimitive?.content ?: ""
                )
                "message.complete" -> StreamEvent.MessageComplete(
                    sessionId, payload?.get("message_id")?.jsonPrimitive?.content
                )
                "thinking.delta" -> StreamEvent.ThinkingDelta(
                    sessionId, payload?.get("text")?.jsonPrimitive?.content ?: ""
                )
                "error" -> StreamEvent.Error(
                    sessionId, payload?.get("message")?.jsonPrimitive?.content ?: "Unknown error"
                )
                else -> null
            }
        } catch (_: Exception) { null }
    }
}
