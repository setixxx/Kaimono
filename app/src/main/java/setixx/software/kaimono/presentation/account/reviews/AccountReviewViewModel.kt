package setixx.software.kaimono.presentation.account.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.usecase.GetProductByIdUseCase
import setixx.software.kaimono.domain.usecase.GetUserReviewsUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class AccountReviewViewModel @Inject constructor(
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {
    private val _state = MutableStateFlow(AccountReviewViewModelState())
    val state: StateFlow<AccountReviewViewModelState> = _state.asStateFlow()

    init {
        loadUserReviews()
    }

    private fun loadUserReviews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getUserReviewsUseCase()) {
                is ApiResult.Success -> {
                    val reviews = result.data
                    _state.update {
                        it.copy(
                            reviews = reviews,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    fetchProductNames(reviews.mapNotNull { it.productPublicId }.distinct())
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

    private fun fetchProductNames(productIds: List<String>) {
        productIds.forEach { productId ->
            if (!_state.value.productNames.containsKey(productId)) {
                viewModelScope.launch {
                    when (val result = getProductByIdUseCase(productId)) {
                        is ApiResult.Success -> {
                            _state.update { currentState ->
                                currentState.copy(
                                    productNames = currentState.productNames + (productId to result.data.name)
                                )
                            }
                        }
                        is ApiResult.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = errorMapper.mapToMessage(result.error)
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
