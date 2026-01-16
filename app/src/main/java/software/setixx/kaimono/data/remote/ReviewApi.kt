package software.setixx.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import software.setixx.kaimono.data.remote.dto.CreateReviewRequest
import software.setixx.kaimono.data.remote.dto.DeleteReviewResponse
import software.setixx.kaimono.data.remote.dto.ReviewResponse
import software.setixx.kaimono.data.remote.dto.UpdateReviewRequest

interface ReviewApi {
    @GET("/products/{id}/reviews")
    suspend fun getProductReviews(@Path("id") productId: String): List<ReviewResponse>
    @POST("/reviews")
    suspend fun createReview(@Body createReviewRequest: CreateReviewRequest): ReviewResponse
    @GET("/reviews/my")
    suspend fun getUsersReview(): List<ReviewResponse>
    @PATCH("/reviews/{id}")
    suspend fun updateReview(@Path("id") reviewPublicId: String, @Body updateReviewRequest: UpdateReviewRequest): ReviewResponse
    @DELETE("/reviews/{id}")
    suspend fun deleteReview(@Path("id") reviewPublicId: String): DeleteReviewResponse
}