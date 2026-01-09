package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Review
import setixx.software.kaimono.domain.model.UpdateReview
import setixx.software.kaimono.domain.repository.ReviewRepository
import javax.inject.Inject

class UpdateReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(reviewId: Long, updateReview: UpdateReview): ApiResult<Review> =
        reviewRepository.updateReview(reviewId, updateReview)
}