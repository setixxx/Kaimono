package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Review
import setixx.software.kaimono.domain.repository.ReviewRepository
import javax.inject.Inject

class GetUserReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(): ApiResult<List<Review>> =
        reviewRepository.getUsersReviews()
}