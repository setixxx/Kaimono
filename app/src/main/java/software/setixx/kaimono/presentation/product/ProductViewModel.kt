package software.setixx.kaimono.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.AddCartItem
import software.setixx.kaimono.domain.model.AddWishListItem
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.Size
import software.setixx.kaimono.domain.model.UpdateCartItem
import software.setixx.kaimono.domain.usecase.AddCartItemUseCase
import software.setixx.kaimono.domain.usecase.AddWishListItemUseCase
import software.setixx.kaimono.domain.usecase.DeleteCartItemUseCase
import software.setixx.kaimono.domain.usecase.DeleteWishListItemUseCase
import software.setixx.kaimono.domain.usecase.GetCartUseCase
import software.setixx.kaimono.domain.usecase.GetProductByIdUseCase
import software.setixx.kaimono.domain.usecase.GetProductReviewsUseCase
import software.setixx.kaimono.domain.usecase.GetUserWishListUseCase
import software.setixx.kaimono.domain.usecase.UpdateCartItemUseCase
import software.setixx.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductReviewsUseCase: GetProductReviewsUseCase,
    private val getUserWishListUseCase: GetUserWishListUseCase,
    private val addWishListItemUseCase: AddWishListItemUseCase,
    private val deleteWishListItemUseCase: DeleteWishListItemUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val addCartItemUseCase: AddCartItemUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
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
            getCart()
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
                    updateQuantityFromCart()
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

    private fun getCart() {
        viewModelScope.launch {
            when (val result = getCartUseCase()) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            cartItems = result.data.items,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    updateQuantityFromCart()
                }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }
                is ApiResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun updateQuantityFromCart() {
        val cartItem = _state.value.currentCartItem
        _state.update { it.copy(quantity = cartItem?.quantity ?: 1) }
    }

    fun onSizeSelected(size: Size) {
        _state.update { it.copy(selectedSize = size) }
        updateQuantityFromCart()
    }

    fun incrementQuantity() {
        val nextQuantity = _state.value.quantity + 1
        if (_state.value.isProductInCart) {
            updateCartQuantity(nextQuantity)
        } else {
            _state.update { it.copy(quantity = nextQuantity) }
        }
    }

    fun decrementQuantity() {
        val currentQuantity = _state.value.quantity
        if (currentQuantity > 1) {
            val nextQuantity = currentQuantity - 1
            if (_state.value.isProductInCart) {
                updateCartQuantity(nextQuantity)
            } else {
                _state.update { it.copy(quantity = nextQuantity) }
            }
        } else if (currentQuantity == 1 && _state.value.isProductInCart) {
            removeFromCart()
        }
    }

    private fun removeFromCart() {
        val cartItem = _state.value.currentCartItem ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = deleteCartItemUseCase(cartItem.productPublicId, cartItem.size)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            cartItems = result.data.items,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    updateQuantityFromCart()
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

    fun toggleFavorite() {
        val currentProduct = _state.value.product ?: return
        viewModelScope.launch {
            if (_state.value.isFavorite) {
                when (val result = deleteWishListItemUseCase(currentProduct.publicId)) {
                    is ApiResult.Success -> _state.update { it.copy(isFavorite = false) }
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
            } else {
                val request = AddWishListItem(currentProduct.publicId)
                when (val result = addWishListItemUseCase(request)) {
                    is ApiResult.Success -> {
                        _state.update { it.copy(isFavorite = true) }
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
    }

    fun addToCart() {
        val product = _state.value.product ?: return
        val size = _state.value.selectedSize ?: return
        val quantity = _state.value.quantity

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val request = AddCartItem(product.publicId, size.size, quantity)
            when (val result = addCartItemUseCase(request)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            cartItems = result.data.items,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    updateQuantityFromCart()
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

    private fun updateCartQuantity(newQuantity: Int) {
        val cartItem = _state.value.currentCartItem ?: return
        if (newQuantity < 1) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val request = UpdateCartItem(cartItem.size, newQuantity)
            when (val result = updateCartItemUseCase(cartItem.productPublicId, request)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            cartItems = result.data.items,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    updateQuantityFromCart()
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
}
