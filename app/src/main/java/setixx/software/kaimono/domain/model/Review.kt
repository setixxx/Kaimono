package setixx.software.kaimono.domain.model

data class Review(
    val id: Long,
    val userName: String,
    val rating: Short,
    val comment: String?,
    val createdAt: String
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