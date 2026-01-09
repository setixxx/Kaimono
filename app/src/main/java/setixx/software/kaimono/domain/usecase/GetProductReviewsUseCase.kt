package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Review
import setixx.software.kaimono.domain.repository.ReviewRepository
import javax.inject.Inject

class GetProductReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(productId: String): ApiResult<List<Review>> =
        reviewRepository.getProductReviews(productId)
}