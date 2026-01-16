package software.setixx.kaimono.presentation.product.reviews

import software.setixx.kaimono.domain.model.Review

data class ReviewsScreenViewModelState(
    val ownReview: Review? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
