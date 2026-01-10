package setixx.software.kaimono.data.remote

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import setixx.software.kaimono.data.remote.dto.CreateReviewRequest
import setixx.software.kaimono.data.remote.dto.DeleteReviewResponse
import setixx.software.kaimono.data.remote.dto.ReviewResponse
import setixx.software.kaimono.data.remote.dto.UpdateReviewRequest

interface ReviewApi {
    @GET("/reviews/{id}/reviews")
    suspend fun getProductReviews(): List<ReviewResponse>
    @POST("/reviews")
    suspend fun createReview(createReviewRequest: CreateReviewRequest): ReviewResponse
    @GET("/reviews/my")
    suspend fun getUsersReview(): List<ReviewResponse>
    @PATCH("/reviews/{id}")
    suspend fun updateReview(@Path("id") reviewId: Long, updateReviewRequest: UpdateReviewRequest): ReviewResponse
    @DELETE("/reviews/{id}")
    suspend fun deleteReview(@Path("id") reviewId: Long): DeleteReviewResponse
}