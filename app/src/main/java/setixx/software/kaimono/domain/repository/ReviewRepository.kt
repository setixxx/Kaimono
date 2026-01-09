package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateReview
import setixx.software.kaimono.domain.model.Review
import setixx.software.kaimono.domain.model.UpdateReview

interface ReviewRepository {
    suspend fun getProductReviews(productId: String): ApiResult<List<Review>>
    suspend fun createReview(createReview: CreateReview): ApiResult<Review>
    suspend fun getUsersReviews(): ApiResult<List<Review>>
    suspend fun updateReview(reviewId: Long, updateReview: UpdateReview): ApiResult<Review>
    suspend fun deleteReview(reviewId: Long): ApiResult<String>
}