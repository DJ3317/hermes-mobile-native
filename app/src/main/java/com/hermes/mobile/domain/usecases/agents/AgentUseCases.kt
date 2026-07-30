package com.hermes.mobile.domain.usecases.agents
import com.hermes.mobile.domain.models.SubAgent
import com.hermes.mobile.domain.repositories.AgentRepository
import javax.inject.Inject

class GetAgentsUseCase @Inject constructor(private val repo: AgentRepository) { suspend operator fun invoke(): List<SubAgent> = repo.getAgents() }
class PauseAgentUseCase @Inject constructor(private val repo: AgentRepository) { suspend operator fun invoke(id: String) = repo.pauseAgent(id) }
class ResumeAgentUseCase @Inject constructor(private val repo: AgentRepository) { suspend operator fun invoke(id: String) = repo.resumeAgent(id) }
