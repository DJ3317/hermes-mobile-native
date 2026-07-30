package com.hermes.mobile.data.repositories

import com.hermes.mobile.data.local.datastore.AuthDataStore
import com.hermes.mobile.data.local.datastore.SettingsDataStore
import com.hermes.mobile.data.mapper.*
import com.hermes.mobile.data.remote.api.HermesApi
import com.hermes.mobile.data.remote.api.dto.*
import com.hermes.mobile.data.remote.websocket.HermesWebSocketClient
import com.hermes.mobile.domain.models.*
import com.hermes.mobile.domain.repositories.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val api: HermesApi,
    private val wsClient: HermesWebSocketClient,
    private val authStore: AuthDataStore
) : ChatRepository {
    override fun streamMessage(sessionId: String, content: String): Flow<StreamEvent> {
        wsClient.sendText(
            """{"jsonrpc":"2.0","id":"r1","method":"prompt.submit","params":{"session_id":"$sessionId","text":"${content.replace("\"","\\\"")}"}}"""
        )
        return wsClient.events
    }

    override suspend fun stopStreaming(sessionId: String) {
        wsClient.sendText("""{"jsonrpc":"2.0","id":"r2","method":"prompt.stop","params":{"session_id":"$sessionId"}}""")
    }

    override suspend fun getMessages(sessionId: String): List<Message> {
        return api.getSessionMessages(sessionId).messages.map { it.toDomain() }
    }

    override suspend fun createSession(): Session {
        return Session(id = "session-${System.currentTimeMillis()}", title = "新对话", createdAt = "", updatedAt = "")
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
class ArtifactRepositoryImpl @Inject constructor() : ArtifactRepository {
    override suspend fun getArtifacts(): List<Artifact> = emptyList()
    override suspend fun deleteArtifact(id: String) = Unit
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
class StarmapRepositoryImpl @Inject constructor() : StarmapRepository {
    override suspend fun getGraph(): List<StarmapNode> = emptyList()
}

@Singleton
class ProjectRepositoryImpl @Inject constructor() : ProjectRepository {
    override suspend fun getProjects(): List<Project> = emptyList()
    override suspend fun getProjectFiles(projectId: String): List<FileEntry> = emptyList()
    override suspend fun getFileContent(path: String): String = ""
}

@Singleton
class FileRepositoryImpl @Inject constructor() : FileRepository {
    override suspend fun listFiles(path: String): List<FileEntry> = emptyList()
    override suspend fun getFileContent(path: String): String = ""
}

@Singleton
class ReviewRepositoryImpl @Inject constructor() : ReviewRepository {
    override suspend fun getReviews(): List<CodeReview> = emptyList()
    override suspend fun getReviewDetail(id: String): CodeReview = CodeReview(id = id, title = "", status = "")
}

@Singleton
class TerminalRepositoryImpl @Inject constructor() : TerminalRepository {
    override suspend fun getSessions(): List<TerminalSession> = listOf(TerminalSession(id = "1"))
    override fun connect(sessionId: String): Flow<String> = kotlinx.coroutines.flow.emptyFlow()
    override suspend fun sendInput(sessionId: String, input: String) = Unit
    override suspend fun disconnect(sessionId: String) = Unit
}

@Singleton
class PreviewRepositoryImpl @Inject constructor() : PreviewRepository {
    override suspend fun getPreview(url: String): PreviewContent = PreviewContent(url = url)
}

@Singleton
class ConfigRepositoryImpl @Inject constructor(
    private val api: HermesApi,
    private val authStore: AuthDataStore
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
        val response = api.login(LoginRequest(username, password))
        authStore.saveToken(response.token)
        return response.token
    }
    override suspend fun logout() { authStore.clearToken() }
    override suspend fun getStatus(): Map<String, String> = mapOf()
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
