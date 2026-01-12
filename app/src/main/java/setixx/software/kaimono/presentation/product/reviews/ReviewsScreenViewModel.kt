package setixx.software.kaimono.presentation.product.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.usecase.DeleteReviewUseCase
import setixx.software.kaimono.domain.usecase.GetProductReviewsUseCase
import setixx.software.kaimono.domain.usecase.GetUserReviewsUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
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
    val state: StateFlow<ReviewsScreenViewModelState> = _state

    init {
        productId?.let {
            loadReviews()
        }
    }

    fun loadReviews() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            val productReviewsResult = productId?.let { getProductReviewsUseCase(it) }
            val userReviewsResult = getUserReviewsUseCase()

            when (productReviewsResult) {
                is ApiResult.Success -> {
                    val reviews = productReviewsResult.data
                    val userReview = if (userReviewsResult is ApiResult.Success) {
                        userReviewsResult.data.find { it.productPublicId == productId }
                    } else null
                    
                    _state.value = _state.value.copy(
                        reviews = reviews.filter { it.publicId != userReview?.publicId },
                        ownReview = userReview,
                        isLoading = false
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(productReviewsResult.error)
                    )
                }
                ApiResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = false)
                }

                else -> {}
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = deleteReviewUseCase(reviewId)) {
                is ApiResult.Success -> {
                    loadReviews()
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                ApiResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}
