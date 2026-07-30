package com.hermes.mobile.domain.repositories

import com.hermes.mobile.domain.models.*
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun streamMessage(sessionId: String, content: String): Flow<StreamEvent>
    suspend fun stopStreaming(sessionId: String)
    suspend fun getMessages(sessionId: String): List<Message>
    suspend fun createSession(): Session
}

interface SessionRepository {
    suspend fun listSessions(limit: Int = 50, offset: Int = 0, archived: Boolean? = null): List<Session>
    suspend fun searchSessions(query: String): List<Session>
    suspend fun getSession(id: String): Session
    suspend fun deleteSession(id: String)
    suspend fun renameSession(id: String, title: String)
    suspend fun setArchived(id: String, archived: Boolean)
    suspend fun setPinned(id: String, pinned: Boolean)
}

interface SkillRepository {
    suspend fun getSkills(): List<Skill>
    suspend fun toggleSkill(name: String, enabled: Boolean)
    suspend fun createSkill(name: String, description: String, content: String): Skill
    suspend fun updateSkill(name: String, description: String, content: String): Skill
    suspend fun getSkillDetail(name: String): Skill
}

interface ProfileRepository {
    suspend fun getProfiles(): List<Profile>
    suspend fun createProfile(name: String, label: String): Profile
    suspend fun deleteProfile(name: String)
    suspend fun switchProfile(name: String)
    suspend fun updateProfileSoul(name: String, soul: String)
}

interface MessagingRepository {
    suspend fun getPlatforms(): List<PlatformGateway>
    suspend fun togglePlatform(id: String, enabled: Boolean)
    suspend fun testPlatform(id: String): Boolean
}

interface ArtifactRepository {
    suspend fun getArtifacts(): List<Artifact>
    suspend fun deleteArtifact(id: String)
}

interface CronRepository {
    suspend fun getTasks(): List<CronTask>
    suspend fun createTask(name: String, schedule: String, prompt: String): CronTask
    suspend fun updateTask(task: CronTask): CronTask
    suspend fun deleteTask(id: String)
    suspend fun toggleTask(id: String, enabled: Boolean)
    suspend fun getTaskHistory(id: String): List<CronRunHistory>
}

interface AgentRepository {
    suspend fun getAgents(): List<SubAgent>
    suspend fun pauseAgent(id: String)
    suspend fun resumeAgent(id: String)
    suspend fun cancelAgent(id: String)
}

interface StarmapRepository {
    suspend fun getGraph(): List<StarmapNode>  // simplified
}

interface ProjectRepository {
    suspend fun getProjects(): List<Project>
    suspend fun getProjectFiles(projectId: String): List<FileEntry>
    suspend fun getFileContent(path: String): String
}

interface FileRepository {
    suspend fun listFiles(path: String): List<FileEntry>
    suspend fun getFileContent(path: String): String
}

interface ReviewRepository {
    suspend fun getReviews(): List<CodeReview>
    suspend fun getReviewDetail(id: String): CodeReview
}

interface TerminalRepository {
    suspend fun getSessions(): List<TerminalSession>
    fun connect(sessionId: String): Flow<String>
    suspend fun sendInput(sessionId: String, input: String)
    suspend fun disconnect(sessionId: String)
}

interface PreviewRepository {
    suspend fun getPreview(url: String): PreviewContent
}

interface ConfigRepository {
    suspend fun getConfig(): Map<String, String>
    suspend fun saveConfig(key: String, value: String)
    suspend fun getModels(refresh: Boolean = false): List<ModelOption>
    suspend fun setModel(provider: String, model: String)
    suspend fun getProviders(): List<Provider>
    suspend fun getMcpServers(): List<McpServer>
    suspend fun toggleMcpServer(name: String, enabled: Boolean)
    suspend fun getTools(): List<ToolConfig>
    suspend fun toggleTool(name: String, enabled: Boolean)
    suspend fun login(username: String, password: String): String
    suspend fun logout()
    suspend fun getStatus(): Map<String, String>
}

interface SearchRepository {
    suspend fun searchAll(query: String): List<SearchResult>
}
