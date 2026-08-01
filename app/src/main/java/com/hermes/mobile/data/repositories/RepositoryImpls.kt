package com.hermes.mobile.data.repositories

import com.hermes.mobile.data.local.AppConfig
import com.hermes.mobile.data.local.datastore.AuthDataStore
import com.hermes.mobile.data.local.datastore.SettingsDataStore
import com.hermes.mobile.data.mapper.*
import com.hermes.mobile.data.remote.api.HermesApi
import com.hermes.mobile.data.remote.api.dto.*
import com.hermes.mobile.data.remote.websocket.HermesWebSocketClient
import com.hermes.mobile.domain.models.*
import com.hermes.mobile.domain.repositories.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val api: HermesApi,
    private val wsClient: HermesWebSocketClient,
    private val authStore: AuthDataStore,
    private val appConfig: AppConfig
) : ChatRepository {
    /** 确保 WebSocket 已连接（URL 带 ticket/token 认证，hermes-agent 标准） */
    private fun ensureConnected(): Boolean {
        if (wsClient.isConnected()) return true
        val host = appConfig.backendHost
        if (host.isBlank()) return false
        // 认证方式：ticket 或 token 作为 URL query 参数（web 端 buildHermesWebSocketUrl 相同方式）
        val authToken: String? = authStore.getToken()
        var authQuery = ""
        try {
            val ticket = kotlinx.coroutines.runBlocking { api.getWsTicket().ticket }
            if (ticket.isNotBlank()) {
                authQuery = "?ticket=$ticket"
            }
        } catch (_: Exception) {
            // ticket 失败则回退 token query
            if (!authToken.isNullOrBlank()) {
                authQuery = "?token=${authToken.removePrefix("Basic ")}"
            }
        }
        val wsUrl = host.replace("http://", "ws://").replace("https://", "wss://").trimEnd('/') + "/api/ws" + authQuery
        return wsClient.connect(wsUrl, authToken)
    }

    override fun streamMessage(sessionId: String, content: String): Flow<StreamEvent> {
        if (!ensureConnected()) {
            return kotlinx.coroutines.flow.flow {
                emit(StreamEvent.Error(sessionId, "WebSocket 未连接，请先测试连接"))
            }
        }
        wsClient.sendText(
            """{"jsonrpc":"2.0","id":"r1","method":"prompt.submit","params":{"session_id":"$sessionId","text":"${content.replace("\"","\\\"")}"}}"""
        )
        return wsClient.events
    }

    override suspend fun stopStreaming(sessionId: String) {
        ensureConnected()
        wsClient.sendText("""{"jsonrpc":"2.0","id":"r2","method":"prompt.stop","params":{"session_id":"$sessionId"}}""")
    }

    override suspend fun getMessages(sessionId: String): List<Message> {
        return api.getSessionMessages(sessionId).messages.map { it.toDomain() }
    }

    override suspend fun createSession(): Session {
        return Session(id = "session-${System.currentTimeMillis()}", title = "新对话")
    }
}

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : SessionRepository {
    override suspend fun listSessions(limit: Int, offset: Int, archived: Boolean?): List<Session> {
        return api.getSessions(limit, offset).sessions.map { it.toDomain() }
    }

    override suspend fun searchSessions(query: String): List<Session> {
        return api.searchSessions(query).sessions.map { it.toDomain() }
    }

    override suspend fun getSession(id: String): Session = api.getSession(id).toDomain()
    override suspend fun deleteSession(id: String) { api.deleteSession(id) }
    override suspend fun renameSession(id: String, title: String) { api.updateSession(id, mapOf("title" to title)) }
    override suspend fun setArchived(id: String, archived: Boolean) { api.updateSession(id, mapOf("archived" to archived)) }
    override suspend fun setPinned(id: String, pinned: Boolean) { api.updateSession(id, mapOf("pinned" to pinned)) }
}

@Singleton
class SkillRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : SkillRepository {
    override suspend fun getSkills(): List<Skill> = api.getSkills().skills.map { it.toDomain() }
    override suspend fun toggleSkill(name: String, enabled: Boolean) { api.toggleSkill(SkillToggleRequest(name, enabled)) }
    override suspend fun createSkill(name: String, description: String, content: String): Skill = Skill(name, description)
    override suspend fun updateSkill(name: String, description: String, content: String): Skill = Skill(name, description)
    override suspend fun getSkillDetail(name: String): Skill = Skill(name, "")
}

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : ProfileRepository {
    override suspend fun getProfiles(): List<Profile> = api.getProfiles().profiles.map { it.toDomain() }
    override suspend fun createProfile(name: String, label: String): Profile = api.createProfile(ProfileCreateRequest(name, label)).toDomain()
    override suspend fun deleteProfile(name: String) { api.deleteProfile(name) }
    override suspend fun switchProfile(name: String) {}
    override suspend fun updateProfileSoul(name: String, soul: String) {}
}

@Singleton
class MessagingRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : MessagingRepository {
    override suspend fun getPlatforms(): List<PlatformGateway> = api.getPlatforms().platforms.map { it.toDomain() }
    override suspend fun togglePlatform(id: String, enabled: Boolean) = Unit
    override suspend fun testPlatform(id: String): Boolean = true
}

@Singleton
class ArtifactRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : ArtifactRepository {
    override suspend fun getArtifacts(): List<Artifact> {
        return try {
            api.getArtifacts().mapNotNull { m ->
                val id = m["id"] as? String ?: return@mapNotNull null
                Artifact(id = id, name = (m["name"] as? String) ?: id, type = (m["type"] as? String) ?: "file")
            }
        } catch (_: Exception) { emptyList() }
    }
    override suspend fun deleteArtifact(id: String) { try { api.deleteArtifact(id) } catch (_: Exception) { } }
}

@Singleton
class CronRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : CronRepository {
    override suspend fun getTasks(): List<CronTask> = api.getCronJobs().jobs.map { it.toDomain() }
    override suspend fun createTask(name: String, schedule: String, prompt: String): CronTask = CronTask(id = "", name = name, schedule = schedule, prompt = prompt)
    override suspend fun updateTask(task: CronTask): CronTask = task
    override suspend fun deleteTask(id: String) { api.deleteCronJob(id) }
    override suspend fun toggleTask(id: String, enabled: Boolean) {
        if (enabled) api.resumeCronJob(id) else api.pauseCronJob(id)
    }
    override suspend fun getTaskHistory(id: String): List<CronRunHistory> = emptyList()
}

@Singleton
class AgentRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : AgentRepository {
    override suspend fun getAgents(): List<SubAgent> = api.getAgents().map { it.toDomain() }
    override suspend fun pauseAgent(id: String) { api.pauseAgent(id) }
    override suspend fun resumeAgent(id: String) { api.resumeAgent(id) }
    override suspend fun cancelAgent(id: String) = Unit
}

@Singleton
class StarmapRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : StarmapRepository {
    override suspend fun getGraph(): List<StarmapNode> {
        return try {
            api.getStarmap().mapNotNull { m ->
                val id = m["id"] as? String ?: return@mapNotNull null
                StarmapNode(id = id, label = (m["label"] as? String) ?: id, type = (m["type"] as? String) ?: "node")
            }
        } catch (_: Exception) { emptyList() }
    }
}

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : ProjectRepository {
    override suspend fun getProjects(): List<Project> {
        return try {
            api.getProjects().mapNotNull { m ->
                val id = m["id"] as? String ?: return@mapNotNull null
                Project(id = id, name = (m["name"] as? String) ?: id)
            }
        } catch (_: Exception) { emptyList() }
    }
    override suspend fun getProjectFiles(projectId: String): List<FileEntry> = emptyList()
    override suspend fun getFileContent(path: String): String = try { api.getFileContent(path) } catch (_: Exception) { "" }
}

@Singleton
class FileRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : FileRepository {
    override suspend fun listFiles(path: String): List<FileEntry> {
        return try {
            api.listFiles(path).mapNotNull { m ->
                val name = (m["name"] as? String) ?: return@mapNotNull null
                FileEntry(name = name, path = (m["path"] as? String) ?: path, isDirectory = (m["is_directory"] as? Boolean) ?: false)
            }
        } catch (_: Exception) { emptyList() }
    }
    override suspend fun getFileContent(path: String): String = try { api.getFileContent(path) } catch (_: Exception) { "" }
}

@Singleton
class ReviewRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : ReviewRepository {
    override suspend fun getReviews(): List<CodeReview> {
        return try {
            api.getReviews().mapNotNull { m ->
                val id = m["id"] as? String ?: return@mapNotNull null
                CodeReview(id = id, title = (m["title"] as? String) ?: id, status = (m["status"] as? String) ?: "")
            }
        } catch (_: Exception) { emptyList() }
    }
    override suspend fun getReviewDetail(id: String): CodeReview = CodeReview(id = id, title = "", status = "")
}

@Singleton
class TerminalRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : TerminalRepository {
    override suspend fun getSessions(): List<TerminalSession> {
        return try {
            api.getTerminalSessions().mapNotNull { m ->
                val id = m["id"] as? String ?: return@mapNotNull null
                TerminalSession(id = id, name = (m["name"] as? String) ?: "Terminal")
            }
        } catch (_: Exception) { emptyList() }
    }
    override fun connect(sessionId: String): Flow<String> = kotlinx.coroutines.flow.emptyFlow()
    override suspend fun sendInput(sessionId: String, input: String) = Unit
    override suspend fun disconnect(sessionId: String) = Unit
}

@Singleton
class PreviewRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : PreviewRepository {
    override suspend fun getPreview(url: String): PreviewContent {
        return try {
            val m = api.getPreview(url)
            PreviewContent(url = url, content = (m["content"] as? String) ?: (m["html"] as? String))
        } catch (_: Exception) { PreviewContent(url = url) }
    }
}

@Singleton
class ConfigRepositoryImpl @Inject constructor(
    private val api: HermesApi,
    private val authStore: AuthDataStore,
    private val settingsDataStore: SettingsDataStore,
    private val appConfig: AppConfig
) : ConfigRepository {
    override suspend fun getConfig(): Map<String, String> = api.getConfig()
    override suspend fun saveConfig(key: String, value: String) { api.saveConfig(mapOf(key to value)) }
    override suspend fun getModels(refresh: Boolean): List<ModelOption> =
        api.getModelOptions(if (refresh) true else null).models.map { it.toDomain() }
    override suspend fun setModel(provider: String, model: String) { api.setModel(ModelSetRequest(provider = provider, model = model)) }
    override suspend fun getProviders(): List<Provider> = emptyList()
    override suspend fun getMcpServers(): List<McpServer> = emptyList()
    override suspend fun toggleMcpServer(name: String, enabled: Boolean) = Unit
    override suspend fun getTools(): List<ToolConfig> = emptyList()
    override suspend fun toggleTool(name: String, enabled: Boolean) = Unit

    override suspend fun login(username: String, password: String): String {
        // 读取用户配置的后端地址
        val host = try { settingsDataStore.settings.first().backendHost } catch (_: Exception) { "" }
        if (host.isBlank()) throw Exception("请先在设置中配置服务器地址")
        appConfig.updateHost(host) // 同步到内存缓存（拦截器使用）
        var jsonError: String? = null

        // 方式1: JSON 登录
        try {
            val response = api.login(LoginRequest(username, password))
            if (response.token.isNotBlank()) {
                authStore.saveToken(response.token)
                return response.token
            }
        } catch (e: Exception) {
            jsonError = e.message ?: "HTTP 登录失败"
        }

        // 方式2: HTTP Basic Auth — 必须在 IO 线程执行网络调用
        val credentials = java.util.Base64.getEncoder().encodeToString("$username:$password".toByteArray())
        return withContext(Dispatchers.IO) {
            try {
                val url = java.net.URL("$host/api/status")
                val conn = url.openConnection() as java.net.HttpURLConnection
                conn.setRequestProperty("Authorization", "Basic $credentials")
                conn.connectTimeout = 10000
                conn.readTimeout = 10000
                try {
                    val code = conn.responseCode
                    if (code in 200..299) {
                        authStore.saveToken("Basic $credentials")
                        "Basic $credentials"
                    } else {
                        throw Exception("HTTP $code${if (jsonError != null) " (JSON: $jsonError)" else ""}")
                    }
                } finally {
                    conn.disconnect()
                }
            } catch (e: Exception) {
                // 合并两个方式的错误信息，确保不为 null
                throw Exception(e.message ?: jsonError ?: "连接失败")
            }
        }
    }

    override suspend fun logout() { authStore.clearToken() }
    override suspend fun getStatus(): Map<String, String> {
        val status = api.getStatus()
        return mapOf(
            "status" to status.status,
            "version" to (status.version ?: "")
        )
    }
}

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val api: HermesApi
) : SearchRepository {
    override suspend fun searchAll(query: String): List<SearchResult> =
        api.searchSessions(query).sessions.map {
            SearchResult(sessionId = it.id, title = it.title, snippet = "")
        }
}
