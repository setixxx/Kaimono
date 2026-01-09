package setixx.software.kaimono.domain.model

data class Cart(
    val id: Long,
    val items: List<CartItem>,
    val totalPrice: String
)

data class CartItem(
    val id: Long,
    val productId: Long,
    val productName: String,
    val productImage: String?,
    val size: String,
    val quantity: Int,
    val pricePerItem: String,
    val subtotal: String
)

data class AddCartItem(
    val productId: String,
    val sizeId: Int,
    val quantity: Int
)

data class UpdateCartItem(
    val quantity: Int
)