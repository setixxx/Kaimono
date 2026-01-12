package setixx.software.kaimono.presentation.product.reviews

import setixx.software.kaimono.domain.model.Review

data class ReviewsScreenViewModelState(
    val ownReview: Review? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
