package software.setixx.kaimono.presentation.product.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.usecase.DeleteReviewUseCase
import software.setixx.kaimono.domain.usecase.GetProductReviewsUseCase
import software.setixx.kaimono.domain.usecase.GetUserReviewsUseCase
import software.setixx.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class ReviewsScreenViewModel @Inject constructor(
    private val getProductReviewsUseCase: GetProductReviewsUseCase,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val errorMapper: ErrorMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val productId: String? = savedStateHandle["productId"]
    private val _state = MutableStateFlow(ReviewsScreenViewModelState())
    val state = _state.asStateFlow()

    init {
        productId?.let {
            loadReviews()
        }
    }

    fun loadReviews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val productReviewsResult = productId?.let { getProductReviewsUseCase(it) }
            val userReviewsResult = getUserReviewsUseCase()

            when (productReviewsResult) {
                is ApiResult.Success -> {
                    val reviews = productReviewsResult.data
                    val userReview = if (userReviewsResult is ApiResult.Success) {
                        userReviewsResult.data.find { it.productPublicId == productId }
                    } else null

                    _state.update {
                        it.copy(
                            reviews = reviews.filter { it.publicId != userReview?.publicId },
                            ownReview = userReview,
                            isLoading = false
                        )
                    }
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(productReviewsResult.error)
                        )
                    }
                }

                ApiResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = deleteReviewUseCase(reviewId)) {
                is ApiResult.Success -> {
                    loadReviews()
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }

                ApiResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun onIsDialogOpen(value: Boolean) {
        _state.update { it.copy(isDialogOpen = value) }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
