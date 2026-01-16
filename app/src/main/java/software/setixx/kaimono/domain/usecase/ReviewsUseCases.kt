package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateReview
import software.setixx.kaimono.domain.model.Review
import software.setixx.kaimono.domain.model.UpdateReview
import software.setixx.kaimono.domain.repository.ReviewRepository
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