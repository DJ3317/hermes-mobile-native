package com.hermes.mobile.di

import com.hermes.mobile.data.remote.api.HermesApi
import com.hermes.mobile.data.remote.websocket.HermesWebSocketClient
import com.hermes.mobile.data.repositories.*
import com.hermes.mobile.domain.repositories.*
import dagger.Module
import dagger.Provides
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.31.250:9191/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .build()
    }

    @Provides
    @Singleton
    fun provideHermesApi(retrofit: Retrofit): HermesApi = retrofit.create(HermesApi::class.java)

    @Provides
    @Singleton
    fun provideWebSocketClient(): HermesWebSocketClient = HermesWebSocketClient()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindChatRepo(impl: ChatRepositoryImpl): ChatRepository
    @Binds @Singleton abstract fun bindSessionRepo(impl: SessionRepositoryImpl): SessionRepository
    @Binds @Singleton abstract fun bindSkillRepo(impl: SkillRepositoryImpl): SkillRepository
    @Binds @Singleton abstract fun bindProfileRepo(impl: ProfileRepositoryImpl): ProfileRepository
    @Binds @Singleton abstract fun bindMessagingRepo(impl: MessagingRepositoryImpl): MessagingRepository
    @Binds @Singleton abstract fun bindArtifactRepo(impl: ArtifactRepositoryImpl): ArtifactRepository
    @Binds @Singleton abstract fun bindCronRepo(impl: CronRepositoryImpl): CronRepository
    @Binds @Singleton abstract fun bindAgentRepo(impl: AgentRepositoryImpl): AgentRepository
    @Binds @Singleton abstract fun bindStarmapRepo(impl: StarmapRepositoryImpl): StarmapRepository
    @Binds @Singleton abstract fun bindProjectRepo(impl: ProjectRepositoryImpl): ProjectRepository
    @Binds @Singleton abstract fun bindFileRepo(impl: FileRepositoryImpl): FileRepository
    @Binds @Singleton abstract fun bindReviewRepo(impl: ReviewRepositoryImpl): ReviewRepository
    @Binds @Singleton abstract fun bindTerminalRepo(impl: TerminalRepositoryImpl): TerminalRepository
    @Binds @Singleton abstract fun bindPreviewRepo(impl: PreviewRepositoryImpl): PreviewRepository
    @Binds @Singleton abstract fun bindConfigRepo(impl: ConfigRepositoryImpl): ConfigRepository
    @Binds @Singleton abstract fun bindSearchRepo(impl: SearchRepositoryImpl): SearchRepository
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // Chat
    @Provides @Singleton fun provideSendMessageUseCase(repo: ChatRepository) = com.hermes.mobile.domain.usecases.chat.SendMessageUseCase(repo)
    @Provides @Singleton fun provideStopStreamingUseCase(repo: ChatRepository) = com.hermes.mobile.domain.usecases.chat.StopStreamingUseCase(repo)
    @Provides @Singleton fun provideGetMessagesUseCase(repo: ChatRepository) = com.hermes.mobile.domain.usecases.chat.GetMessagesUseCase(repo)
    @Provides @Singleton fun provideStreamMessageUseCase(repo: ChatRepository) = com.hermes.mobile.domain.usecases.chat.StreamMessageUseCase(repo)
    // Sessions
    @Provides @Singleton fun provideGetSessionsUseCase(repo: SessionRepository) = com.hermes.mobile.domain.usecases.sessions.GetSessionsUseCase(repo)
    @Provides @Singleton fun provideSearchSessionsUseCase(repo: SessionRepository) = com.hermes.mobile.domain.usecases.sessions.SearchSessionsUseCase(repo)
    @Provides @Singleton fun provideDeleteSessionUseCase(repo: SessionRepository) = com.hermes.mobile.domain.usecases.sessions.DeleteSessionUseCase(repo)
    @Provides @Singleton fun provideRenameSessionUseCase(repo: SessionRepository) = com.hermes.mobile.domain.usecases.sessions.RenameSessionUseCase(repo)
    // Skills
    @Provides @Singleton fun provideGetSkillsUseCase(repo: SkillRepository) = com.hermes.mobile.domain.usecases.skills.GetSkillsUseCase(repo)
    @Provides @Singleton fun provideToggleSkillUseCase(repo: SkillRepository) = com.hermes.mobile.domain.usecases.skills.ToggleSkillUseCase(repo)
    // Settings
    @Provides @Singleton fun provideGetModelsUseCase(repo: ConfigRepository) = com.hermes.mobile.domain.usecases.settings.GetModelsUseCase(repo)
    @Provides @Singleton fun provideSetModelUseCase(repo: ConfigRepository) = com.hermes.mobile.domain.usecases.settings.SetModelUseCase(repo)
    @Provides @Singleton fun provideLoginUseCase(repo: ConfigRepository) = com.hermes.mobile.domain.usecases.settings.LoginUseCase(repo)
    @Provides @Singleton fun provideLogoutUseCase(repo: ConfigRepository) = com.hermes.mobile.domain.usecases.settings.LogoutUseCase(repo)
    // Profiles
    @Provides @Singleton fun provideGetProfilesUseCase(repo: ProfileRepository) = com.hermes.mobile.domain.usecases.profiles.GetProfilesUseCase(repo)
    @Provides @Singleton fun provideCreateProfileUseCase(repo: ProfileRepository) = com.hermes.mobile.domain.usecases.profiles.CreateProfileUseCase(repo)
    @Provides @Singleton fun provideDeleteProfileUseCase(repo: ProfileRepository) = com.hermes.mobile.domain.usecases.profiles.DeleteProfileUseCase(repo)
    // Artifacts
    @Provides @Singleton fun provideGetArtifactsUseCase(repo: ArtifactRepository) = com.hermes.mobile.domain.usecases.artifacts.GetArtifactsUseCase(repo)
    @Provides @Singleton fun provideDeleteArtifactUseCase(repo: ArtifactRepository) = com.hermes.mobile.domain.usecases.artifacts.DeleteArtifactUseCase(repo)
    // Cron
    @Provides @Singleton fun provideGetCronTasksUseCase(repo: CronRepository) = com.hermes.mobile.domain.usecases.cron.GetCronTasksUseCase(repo)
    @Provides @Singleton fun provideToggleCronTaskUseCase(repo: CronRepository) = com.hermes.mobile.domain.usecases.cron.ToggleCronTaskUseCase(repo)
    // Agents
    @Provides @Singleton fun provideGetAgentsUseCase(repo: AgentRepository) = com.hermes.mobile.domain.usecases.agents.GetAgentsUseCase(repo)
    @Provides @Singleton fun providePauseAgentUseCase(repo: AgentRepository) = com.hermes.mobile.domain.usecases.agents.PauseAgentUseCase(repo)
    @Provides @Singleton fun provideResumeAgentUseCase(repo: AgentRepository) = com.hermes.mobile.domain.usecases.agents.ResumeAgentUseCase(repo)
}
