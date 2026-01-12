package setixx.software.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewRequest(
    @SerialName("product_public_id")
    val productPublicId: String,

    @SerialName("order_public_id")
    val orderPublicId: String?,

    val rating: Short,
    val comment: String? = null
)

@Serializable
data class ReviewResponse(
    val id: Long,

    @SerialName("public_id")
    val publicId: String,

    @SerialName("user_name")
    val userName: String,

    val rating: Short,
    val comment: String?,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("product_public_id")
    val productPublicId: String? = null,

    @SerialName("product_name")
    val productName: String? = null,

    @SerialName("product_image")
    val productImage: String? = null
)

@Serializable
data class UpdateReviewRequest(
    val rating: Short,
    val comment: String? = null
)

@Serializable
data class DeleteReviewResponse(
    val message: String
)