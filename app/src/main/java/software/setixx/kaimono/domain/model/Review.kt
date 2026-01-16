package software.setixx.kaimono.domain.model

data class Review(
    val id: Long,
    val publicId: String,
    val userName: String,
    val rating: Short,
    val comment: String?,
    val createdAt: String,
    val productPublicId: String? = null
)

data class CreateReview(
    val productPublicId: String,
    val orderPublicId: String,
    val rating: Short,
    val comment: String?
)

data class UpdateReview(
    val rating: Short,
    val comment: String?
)
