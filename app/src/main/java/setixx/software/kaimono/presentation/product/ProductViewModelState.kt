package setixx.software.kaimono.presentation.product

import setixx.software.kaimono.domain.model.CartItem
import setixx.software.kaimono.domain.model.Product
import setixx.software.kaimono.domain.model.Review
import setixx.software.kaimono.domain.model.Size

data class ProductViewModelState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedSize: Size? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val cartItems: List<CartItem> = emptyList()
) {
    val isProductInCart: Boolean
        get() = cartItems.any { it.productPublicId == product?.publicId && it.size == selectedSize?.size }

    val currentCartItem: CartItem?
        get() = cartItems.find { it.productPublicId == product?.publicId && it.size == selectedSize?.size }
}
