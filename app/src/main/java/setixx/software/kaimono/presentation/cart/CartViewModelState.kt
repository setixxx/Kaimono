package setixx.software.kaimono.presentation.cart

data class CartViewModelState(
    val id: Int = 0,
    val quantity: Int = 0,
    val name: String = "",
    val price: Double = 0.00
)
