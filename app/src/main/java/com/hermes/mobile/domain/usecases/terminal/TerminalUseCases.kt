package com.hermes.mobile.domain.usecases.terminal
import com.hermes.mobile.domain.models.TerminalSession
import com.hermes.mobile.domain.repositories.TerminalRepository
import javax.inject.Inject

class GetTerminalSessionsUseCase @Inject constructor(private val repo: TerminalRepository) { suspend operator fun invoke(): List<TerminalSession> = repo.getSessions() }
