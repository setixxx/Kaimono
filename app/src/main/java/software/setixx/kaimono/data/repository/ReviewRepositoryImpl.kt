package software.setixx.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import software.setixx.kaimono.data.remote.ReviewApi
import software.setixx.kaimono.data.remote.dto.CreateReviewRequest
import software.setixx.kaimono.data.remote.dto.UpdateReviewRequest
import software.setixx.kaimono.domain.error.DomainError
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateReview
import software.setixx.kaimono.domain.model.Review
import software.setixx.kaimono.domain.model.UpdateReview
import software.setixx.kaimono.domain.repository.ReviewRepository
import java.io.IOException
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi
) : ReviewRepository {
    override suspend fun getProductReviews(productId: String): ApiResult<List<Review>> {
        return try {
            val response = reviewApi.getProductReviews(productId)
            val reviews = response.map {
                Review(
                    id = it.id,
                    publicId = it.publicId,
                    userName = it.userName,
                    rating = it.rating,
                    comment = it.comment,
                    createdAt = it.createdAt,
                    productPublicId = it.productPublicId
                )
            }
            ApiResult.Success(reviews)
        } catch (e: HttpException) {
        val error = when (e.code()) {
            400 -> DomainError.InvalidData
            401 -> DomainError.InvalidToken
            500 -> DomainError.ServerInternal
            else -> DomainError.HttpError(e.code(), e.message())
        }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun createReview(createReview: CreateReview): ApiResult<Review> {
        return try {
            val request = CreateReviewRequest(
                productPublicId = createReview.productPublicId,
                orderPublicId = createReview.orderPublicId,
                rating = createReview.rating,
                comment = createReview.comment
            )
            val response = reviewApi.createReview(request)
            ApiResult.Success(
                Review(
                    id = response.id,
                    publicId = response.publicId,
                    userName = response.userName,
                    rating = response.rating,
                    comment = response.comment,
                    createdAt = response.createdAt,
                    productPublicId = response.productPublicId
                )
            )
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun getUserReviews(): ApiResult<List<Review>> {
        return try {
            val response = reviewApi.getUsersReview()
            val reviews = response.map {
                Review(
                    id = it.id,
                    publicId = it.publicId,
                    userName = it.userName,
                    rating = it.rating,
                    comment = it.comment,
                    createdAt = it.createdAt,
                    productPublicId = it.productPublicId
                )
            }
            ApiResult.Success(reviews)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun updateReview(
        reviewPublicId: String,
        updateReview: UpdateReview
    ): ApiResult<Review> {
        return try {
            val request = UpdateReviewRequest(
                rating = updateReview.rating,
                comment = updateReview.comment
            )
            val response = reviewApi.updateReview(reviewPublicId, request)
            ApiResult.Success(
                Review(
                    id = response.id,
                    publicId = response.publicId,
                    userName = response.userName,
                    rating = response.rating,
                    comment = response.comment,
                    createdAt = response.createdAt,
                    productPublicId = response.productPublicId
                )
            )
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun deleteReview(reviewPublicId: String): ApiResult<String> {
        return try {
            val response = reviewApi.deleteReview(reviewPublicId)
            ApiResult.Success(response.message)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }
}
