package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateReview
import software.setixx.kaimono.domain.model.Review
import software.setixx.kaimono.domain.model.UpdateReview

interface ReviewRepository {
    suspend fun getProductReviews(productId: String): ApiResult<List<Review>>
    suspend fun createReview(createReview: CreateReview): ApiResult<Review>
    suspend fun getUserReviews(): ApiResult<List<Review>>
    suspend fun updateReview(reviewPublicId: String, updateReview: UpdateReview): ApiResult<Review>
    suspend fun deleteReview(reviewPublicId: String): ApiResult<String>
}