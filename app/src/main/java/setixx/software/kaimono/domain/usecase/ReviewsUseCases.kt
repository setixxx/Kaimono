package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateReview
import setixx.software.kaimono.domain.model.Review
import setixx.software.kaimono.domain.model.UpdateReview
import setixx.software.kaimono.domain.repository.ReviewRepository
import javax.inject.Inject

class GetProductReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(productId: String): ApiResult<List<Review>> =
        reviewRepository.getProductReviews(productId)
}

class CreateReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(createReview: CreateReview): ApiResult<Review> =
        reviewRepository.createReview(createReview)
}

class GetUserReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(): ApiResult<List<Review>> =
        reviewRepository.getUserReviews()
}

class UpdateReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(reviewPublicId: String, updateReview: UpdateReview): ApiResult<Review> =
        reviewRepository.updateReview(reviewPublicId, updateReview)
}

class DeleteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(reviewPublicId: String): ApiResult<String> =
        reviewRepository.deleteReview(reviewPublicId)
}