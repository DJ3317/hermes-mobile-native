package com.hermes.mobile.data.remote.websocket

import com.hermes.mobile.domain.models.StreamEvent
import com.hermes.mobile.domain.models.ToolCall
import com.hermes.mobile.domain.models.ToolCallStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray
import okhttp3.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HermesWebSocketClient @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val eventChannel = Channel<StreamEvent>(Channel.BUFFERED)
    private val pendingRequests = ConcurrentHashMap<String, Channel<JsonObject>>()
    private var requestId = 0
    private var connected = false

    val events: Flow<StreamEvent> = eventChannel.receiveAsFlow()

    private val json = Json { ignoreUnknownKeys = true }

    fun connect(url: String, token: String? = null): Boolean {
        if (connected) return true

        val requestBuilder = Request.Builder().url(url)
        if (token != null) {
            // hermes-agent 标准认证 header（REST 和 WebSocket 通用）
            requestBuilder.addHeader("X-Hermes-Session-Token", token)
            // 同时保留 Authorization Bearer 作为兼容回退
            if (!token.startsWith("Basic ")) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            } else {
                requestBuilder.addHeader("Authorization", token)
            }
        }

        webSocket = client.newWebSocket(requestBuilder.build(), object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                connected = true
            }

            override fun onMessage(ws: WebSocket, text: String) {
                handleMessage(text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                connected = false
                eventChannel.trySend(StreamEvent.Error("", t.message ?: "WebSocket error"))
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                connected = false
            }
        })
        return true
    }

    fun disconnect() {
        webSocket?.close(1000, "Client closed")
        webSocket = null
        connected = false
    }

    fun isConnected(): Boolean = connected

    suspend fun request(method: String, params: Map<String, Any?> = emptyMap()): JsonObject? {
        val id = "r${++requestId}"
        val requestJson = buildString {
            append("""{"jsonrpc":"2.0","id":"$id","method":"$method","params":""")
            append(json.encodeToString(kotlinx.serialization.serializer<Map<String, kotlinx.serialization.json.JsonElement>>(), emptyMap()))
            append("}")
        }
        webSocket?.send(requestJson)
        return null // simplified
    }

    fun sendText(text: String) {
        webSocket?.send(text)
    }

    private fun handleMessage(text: String) {
        try {
            val root = json.parseToJsonElement(text).jsonObject
            if (root.containsKey("method") && root["method"]?.jsonPrimitive?.content == "event") {
                val params = root["params"]?.jsonObject ?: return
                val type = params["type"]?.jsonPrimitive?.content ?: return
                val sessionId = params["session_id"]?.jsonPrimitive?.content ?: ""
                val payload = params["payload"]?.jsonObject

                when (type) {
                    "message.start" -> eventChannel.trySend(StreamEvent.MessageStart(sessionId))
                    "message.delta" -> {
                        val delta = payload?.get("text")?.jsonPrimitive?.content ?: ""
                        val msgId = payload?.get("message_id")?.jsonPrimitive?.content
                        eventChannel.trySend(StreamEvent.MessageDelta(sessionId, msgId, delta))
                    }
                    "message.complete" -> {
                        val msgId = payload?.get("message_id")?.jsonPrimitive?.content
                        eventChannel.trySend(StreamEvent.MessageComplete(sessionId, msgId))
                    }
                    "thinking.delta" -> {
                        val delta = payload?.get("text")?.jsonPrimitive?.content ?: ""
                        eventChannel.trySend(StreamEvent.ThinkingDelta(sessionId, delta))
                    }
                    "error" -> {
                        val msg = payload?.get("message")?.jsonPrimitive?.content ?: "Unknown error"
                        eventChannel.trySend(StreamEvent.Error(sessionId, msg))
                    }
                }
            }
        } catch (_: Exception) { }
    }
}
