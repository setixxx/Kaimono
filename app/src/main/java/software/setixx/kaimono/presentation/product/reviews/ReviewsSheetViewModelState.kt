package software.setixx.kaimono.presentation.product.reviews

data class ReviewsSheetViewModelState(
    val reviewText: String = "",
    val rating: Int = 1,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val reviewId: String? = null,
    val productPublicId: String? = null,
    val orderPublicId: String? = null
)
