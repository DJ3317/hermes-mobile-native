package com.hermes.mobile.domain.usecases.settings
import com.hermes.mobile.domain.models.ModelOption
import com.hermes.mobile.domain.repositories.ConfigRepository
import javax.inject.Inject

class GetModelsUseCase @Inject constructor(private val repo: ConfigRepository) { suspend operator fun invoke(refresh: Boolean = false): List<ModelOption> = repo.getModels(refresh) }
class SetModelUseCase @Inject constructor(private val repo: ConfigRepository) { suspend operator fun invoke(provider: String, model: String) = repo.setModel(provider, model) }
class LoginUseCase @Inject constructor(private val repo: ConfigRepository) { suspend operator fun invoke(username: String, password: String): String = repo.login(username, password) }
class LogoutUseCase @Inject constructor(private val repo: ConfigRepository) { suspend operator fun invoke() = repo.logout() }
