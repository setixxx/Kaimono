package software.setixx.kaimono.presentation.account.reviews

import software.setixx.kaimono.domain.model.Review

data class AccountReviewViewModelState(
    val reviews: List<Review> = emptyList(),
    val productNames: Map<String, String> = emptyMap(),
    val productImages: Map<String, String?> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
