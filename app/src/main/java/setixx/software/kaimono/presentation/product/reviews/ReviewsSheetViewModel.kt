package setixx.software.kaimono.presentation.product.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateReview
import setixx.software.kaimono.domain.model.Review
import setixx.software.kaimono.domain.model.UpdateReview
import setixx.software.kaimono.domain.usecase.CreateReviewUseCase
import setixx.software.kaimono.domain.usecase.UpdateReviewUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class ReviewsSheetViewModel @Inject constructor(
    private val updateReviewUseCase: UpdateReviewUseCase,
    private val createReviewUseCase: CreateReviewUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {
    private val _state = MutableStateFlow(ReviewsSheetViewModelState())
    val state = _state.asStateFlow()

    fun setReview(review: Review) {
        _state.update {
            it.copy(
                reviewId = review.publicId,
                reviewText = review.comment ?: "",
                rating = review.rating.toInt()
            )
        }
    }

    fun setInitialData(productPublicId: String, orderPublicId: String) {
        _state.update {
            it.copy(
                productPublicId = productPublicId,
                orderPublicId = orderPublicId
            )
        }
    }

    fun onReviewTextChange(text: String) {
        _state.update { it.copy(reviewText = text) }
    }

    fun onRatingChange(rating: Int) {
        _state.update { it.copy(rating = rating) }
    }

    fun updateReview() {
        val reviewId = _state.value.reviewId ?: return
        val updateReview = UpdateReview(
            rating = _state.value.rating.toShort(),
            comment = _state.value.reviewText
        )

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = updateReviewUseCase(reviewId, updateReview)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
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

    fun  createReview() {
        val productPublicId = _state.value.productPublicId ?: return
        val orderPublicId = _state.value.orderPublicId ?: return
        
        val createReview = CreateReview(
            productPublicId = productPublicId,
            orderPublicId = orderPublicId,
            rating = _state.value.rating.toShort(),
            comment = _state.value.reviewText
        )

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = createReviewUseCase(createReview)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
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

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun resetSuccess() {
        _state.update { it.copy(isSuccess = false) }
    }
}
