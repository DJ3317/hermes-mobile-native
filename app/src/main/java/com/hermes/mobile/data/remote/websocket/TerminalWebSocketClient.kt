package com.hermes.mobile.data.remote.websocket

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

/** 终端专用 WebSocket 客户端 — 复用 JSON-RPC 通道 */
@Singleton
class TerminalWebSocketClient @Inject constructor() {

    private val client = OkHttpClient.Builder().build()
    private var webSocket: WebSocket? = null
    private val lineChannel = Channel<String>(Channel.BUFFERED)
    val lines: Flow<String> = lineChannel.receiveAsFlow()

    fun connect(url: String, sessionId: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(ws: WebSocket, text: String) {
                lineChannel.trySend(text)
            }
            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                lineChannel.trySend("[连接断开: ${t.message}]")
            }
        })
        webSocket?.send("""{"jsonrpc":"2.0","id":"1","method":"terminal.connect","params":{"session_id":"$sessionId"}}""")
    }

    fun send(input: String) {
        webSocket?.send("""{"jsonrpc":"2.0","id":"1","method":"terminal.input","params":{"input":"${input.replace("\"","\\\"")}"}}""")
    }

    fun disconnect() { webSocket?.close(1000, "Client closed"); webSocket = null }
}
