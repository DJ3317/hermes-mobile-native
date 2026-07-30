package com.hermes.mobile.domain.usecases.starmap
import com.hermes.mobile.domain.models.StarmapNode
import com.hermes.mobile.domain.repositories.StarmapRepository
import javax.inject.Inject

class GetStarmapUseCase @Inject constructor(private val repo: StarmapRepository) {
    suspend operator fun invoke(): List<StarmapNode> = repo.getGraph()
}
