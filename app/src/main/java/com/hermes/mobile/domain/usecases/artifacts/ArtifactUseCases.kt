package com.hermes.mobile.domain.usecases.artifacts
import com.hermes.mobile.domain.models.Artifact
import com.hermes.mobile.domain.repositories.ArtifactRepository
import javax.inject.Inject

class GetArtifactsUseCase @Inject constructor(private val repo: ArtifactRepository) {
    suspend operator fun invoke(): List<Artifact> = repo.getArtifacts()
}
class DeleteArtifactUseCase @Inject constructor(private val repo: ArtifactRepository) {
    suspend operator fun invoke(id: String) = repo.deleteArtifact(id)
}
