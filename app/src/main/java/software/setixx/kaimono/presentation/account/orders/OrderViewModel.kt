package software.setixx.kaimono.presentation.account.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.usecase.GetOrderByIdUseCase
import software.setixx.kaimono.domain.usecase.GetUserReviewsUseCase
import software.setixx.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
    private val errorMapper: ErrorMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(OrderViewModelState())
    val state: StateFlow<OrderViewModelState> = _state.asStateFlow()

    private val orderId: String? = savedStateHandle["orderId"]

    init {
        orderId?.let {
            loadOrder(it)
        }
    }

    fun refresh() {
        orderId?.let {
            loadOrder(it)
        }
    }

    fun loadOrder(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getOrderByIdUseCase(id)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            order = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    loadWrittenReviews()
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }

                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun loadWrittenReviews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getUserReviewsUseCase()) {
                is ApiResult.Success -> {
                    val userReviews = result.data
                    _state.update { currentState ->
                        val updatedMap = currentState.order?.items?.associateWith { item ->
                            userReviews.any { it.productPublicId == item.productPublicId }
                        } ?: emptyMap()

                        currentState.copy(
                            writtenProductReviews = updatedMap,
                            isLoading = false
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

                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
