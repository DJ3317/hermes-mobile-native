package com.hermes.mobile.domain.usecases.preview
import com.hermes.mobile.domain.models.PreviewContent
import com.hermes.mobile.domain.repositories.PreviewRepository
import javax.inject.Inject

class GetPreviewUseCase @Inject constructor(private val repo: PreviewRepository) { suspend operator fun invoke(url: String): PreviewContent = repo.getPreview(url) }
