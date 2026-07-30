package com.hermes.mobile.domain.usecases.sessions

import com.hermes.mobile.domain.models.Session
import com.hermes.mobile.domain.repositories.SessionRepository
import javax.inject.Inject

class GetSessionsUseCase @Inject constructor(private val repo: SessionRepository) {
    suspend operator fun invoke(limit: Int = 50, offset: Int = 0): List<Session> = repo.listSessions(limit, offset)
}

class SearchSessionsUseCase @Inject constructor(private val repo: SessionRepository) {
    suspend operator fun invoke(query: String): List<Session> = repo.searchSessions(query)
}

class DeleteSessionUseCase @Inject constructor(private val repo: SessionRepository) {
    suspend operator fun invoke(id: String) = repo.deleteSession(id)
}

class RenameSessionUseCase @Inject constructor(private val repo: SessionRepository) {
    suspend operator fun invoke(id: String, title: String) = repo.renameSession(id, title)
}
