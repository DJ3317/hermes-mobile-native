package com.hermes.mobile.data.remote.api

import com.hermes.mobile.data.remote.api.dto.*
import retrofit2.http.*

interface HermesApi {

    // Status
    @GET("api/status")
    suspend fun getStatus(): StatusDto

    // Auth
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/ws-ticket")
    suspend fun getWsTicket(): WsTicketResponse

    // Sessions
    @GET("api/sessions")
    suspend fun getSessions(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): SessionListResponse

    @GET("api/sessions/search")
    suspend fun searchSessions(@Query("q") query: String): SessionListResponse

    @GET("api/sessions/{id}")
    suspend fun getSession(@Path("id") id: String): SessionDto

    @GET("api/sessions/{id}/messages")
    suspend fun getSessionMessages(@Path("id") id: String): MessageListResponse

    @DELETE("api/sessions/{id}")
    suspend fun deleteSession(@Path("id") id: String)

    @PATCH("api/sessions/{id}")
    suspend fun updateSession(@Path("id") id: String, @Body body: Map<String, @JvmSuppressWildcards Any>)

    // Model
    @GET("api/model/options")
    suspend fun getModelOptions(@Query("refresh") refresh: Boolean? = null): ModelOptionResponse

    @POST("api/model/set")
    suspend fun setModel(@Body request: ModelSetRequest)

    // Skills
    @GET("api/skills")
    suspend fun getSkills(): SkillListResponse

    @PUT("api/skills/toggle")
    suspend fun toggleSkill(@Body request: SkillToggleRequest)

    // Profiles
    @GET("api/profiles")
    suspend fun getProfiles(): ProfileListResponse

    @POST("api/profiles")
    suspend fun createProfile(@Body request: ProfileCreateRequest): ProfileDto

    @DELETE("api/profiles/{name}")
    suspend fun deleteProfile(@Path("name") name: String)

    // Messaging
    @GET("api/messaging/platforms")
    suspend fun getPlatforms(): GatewayListResponse

    // Config
    @GET("api/config")
    suspend fun getConfig(): Map<String, String>

    @POST("api/config")
    suspend fun saveConfig(@Body config: Map<String, @JvmSuppressWildcards Any>)

    // Cron
    @GET("api/cron/jobs")
    suspend fun getCronJobs(): CronTaskListResponse

    @POST("api/cron/jobs")
    suspend fun createCronJob(@Body body: Map<String, @JvmSuppressWildcards Any>): CronTaskDto

    @DELETE("api/cron/jobs/{id}")
    suspend fun deleteCronJob(@Path("id") id: String)

    @POST("api/cron/jobs/{id}/pause")
    suspend fun pauseCronJob(@Path("id") id: String)

    @POST("api/cron/jobs/{id}/resume")
    suspend fun resumeCronJob(@Path("id") id: String)

    // Agents
    @GET("api/agents")
    suspend fun getAgents(): List<AgentDto>

    @POST("api/agents/{id}/pause")
    suspend fun pauseAgent(@Path("id") id: String)

    @POST("api/agents/{id}/resume")
    suspend fun resumeAgent(@Path("id") id: String)

    // Artifacts
    @GET("api/artifacts")
    suspend fun getArtifacts(): List<Map<String, @JvmSuppressWildcards Any>>

    @DELETE("api/artifacts/{id}")
    suspend fun deleteArtifact(@Path("id") id: String)

    // Projects
    @GET("api/projects")
    suspend fun getProjects(): List<Map<String, @JvmSuppressWildcards Any>>

    // Files
    @GET("api/files")
    suspend fun listFiles(@Query("path") path: String): List<Map<String, @JvmSuppressWildcards Any>>

    @GET("api/files/content")
    suspend fun getFileContent(@Query("path") path: String): String

    // Review
    @GET("api/reviews")
    suspend fun getReviews(): List<Map<String, @JvmSuppressWildcards Any>>

    // Terminal
    @GET("api/terminal/sessions")
    suspend fun getTerminalSessions(): List<Map<String, @JvmSuppressWildcards Any>>

    // Starmap
    @GET("api/starmap")
    suspend fun getStarmap(): List<Map<String, @JvmSuppressWildcards Any>>

    // Preview
    @GET("api/preview")
    suspend fun getPreview(@Query("url") url: String): Map<String, @JvmSuppressWildcards Any>
}
