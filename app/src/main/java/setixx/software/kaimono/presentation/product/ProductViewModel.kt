package setixx.software.kaimono.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Size
import setixx.software.kaimono.domain.usecase.GetProductByIdUseCase
import setixx.software.kaimono.domain.usecase.GetProductReviewsUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductReviewsUseCase: GetProductReviewsUseCase,
    private val errorMapper: ErrorMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String? = savedStateHandle["productId"]

    private val _state = MutableStateFlow(ProductViewModelState())
    val state = _state.asStateFlow()

    init {
        productId?.let { id ->
            getProduct(id)
            getReviews(id)
        }
    }

    private fun getProduct(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getProductByIdUseCase(id)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            product = result.data,
                            isLoading = false,
                            selectedSize = result.data.sizes.firstOrNull()
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
                ApiResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun getReviews(id: String) {
        viewModelScope.launch {
            when (val result = getProductReviewsUseCase(id)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(reviews = result.data) }
                }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
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

    fun onSizeSelected(size: Size) {
        _state.update { it.copy(selectedSize = size) }
    }

    fun incrementQuantity() {
        _state.update { it.copy(quantity = it.quantity + 1) }
    }

    fun decrementQuantity() {
        _state.update {
            if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
        }
    }

/*    fun addToCart() {
        val currentProduct = _state.value.product ?: return
        val selectedSize = _state.value.selectedSize ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val request = AddCartItem(
                productId = currentProduct.publicId,
                sizeId = selectedSize.id.toInt(),
                quantity = _state.value.quantity
            )
            when (val result = addItemToCartUseCase(request)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    // Maybe show some success message or navigate
                }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }
                ApiResult.Loading -> {}
            }
        }
    }*/

/*    fun toggleFavorite() {
        val currentProduct = _state.value.product ?: return
        viewModelScope.launch {
            if (_state.value.isFavorite) {
                when (deleteWishListItemUseCase(currentProduct.publicId)) {
                    is ApiResult.Success -> _state.update { it.copy(isFavorite = false) }
                    else -> {}
                }
            } else {
                when (addWishListItemUseCase(currentProduct.publicId)) {
                    is ApiResult.Success -> _state.update { it.copy(isFavorite = true) }
                    else -> {}
                }
            }
        }
    }*/

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
