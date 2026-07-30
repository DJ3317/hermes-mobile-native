package com.hermes.mobile.domain.usecases.messaging
import com.hermes.mobile.domain.models.PlatformGateway
import com.hermes.mobile.domain.repositories.MessagingRepository
import javax.inject.Inject

class GetGatewaysUseCase @Inject constructor(private val repo: MessagingRepository) {
    suspend operator fun invoke(): List<PlatformGateway> = repo.getPlatforms()
}
class ToggleGatewayUseCase @Inject constructor(private val repo: MessagingRepository) {
    suspend operator fun invoke(id: String, enabled: Boolean) = repo.togglePlatform(id, enabled)
}
