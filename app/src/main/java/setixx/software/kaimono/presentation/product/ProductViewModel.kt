package setixx.software.kaimono.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.AddWishListItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Size
import setixx.software.kaimono.domain.usecase.AddWishListItemUseCase
import setixx.software.kaimono.domain.usecase.DeleteWishListItemUseCase
import setixx.software.kaimono.domain.usecase.GetProductByIdUseCase
import setixx.software.kaimono.domain.usecase.GetProductReviewsUseCase
import setixx.software.kaimono.domain.usecase.GetUserWishListUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductReviewsUseCase: GetProductReviewsUseCase,
    private val getUserWishListUseCase: GetUserWishListUseCase,
    private val addWishListItemUseCase: AddWishListItemUseCase,
    private val deleteWishListItemUseCase: DeleteWishListItemUseCase,
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
            val productResult = getProductByIdUseCase(id)
            val wishlistResult = getUserWishListUseCase()

            when (productResult) {
                is ApiResult.Success -> {
                    val product = productResult.data
                    val isFavorite = if (wishlistResult is ApiResult.Success) {
                        wishlistResult.data.wishListItem.any { it.productPublicId == product.publicId }
                    } else {
                        false
                    }
                    _state.update {
                        it.copy(
                            product = product,
                            isLoading = false,
                            isFavorite = isFavorite,
                            selectedSize = product.sizes.firstOrNull()
                        )
                    }
                }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(productResult.error)
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
                    _state.update {
                        it.copy(
                            reviews = result.data,
                            isLoading = false,
                            errorMessage = null
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

    fun toggleFavorite() {
        val currentProduct = _state.value.product ?: return
        viewModelScope.launch {
            if (_state.value.isFavorite) {
                when (val result = deleteWishListItemUseCase(currentProduct.publicId)) {
                    is ApiResult.Success -> _state.update { it.copy(isFavorite = false) }
                    is ApiResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                    ApiResult.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            } else {
                val request = AddWishListItem(currentProduct.publicId)
                when (val result = addWishListItemUseCase(request)) {
                    is ApiResult.Success -> {
                        _state.update { it.copy(isFavorite = true) }
                    }
                    is ApiResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                    ApiResult.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
