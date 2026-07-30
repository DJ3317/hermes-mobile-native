package com.hermes.mobile.domain.usecases.review
import com.hermes.mobile.domain.models.CodeReview
import com.hermes.mobile.domain.repositories.ReviewRepository
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(private val repo: ReviewRepository) { suspend operator fun invoke(): List<CodeReview> = repo.getReviews() }
class GetReviewDetailUseCase @Inject constructor(private val repo: ReviewRepository) { suspend operator fun invoke(id: String): CodeReview = repo.getReviewDetail(id) }
