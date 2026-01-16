package software.setixx.kaimono.presentation.cart

import software.setixx.kaimono.domain.model.Address
import software.setixx.kaimono.domain.model.Cart
import software.setixx.kaimono.domain.model.CartItem
import software.setixx.kaimono.domain.model.PaymentMethod

data class CartViewModelState(
    val cart: Cart = Cart(0, emptyList(), ""),
    val items: List<CartItem> = emptyList(),
    val totalPrice: String = "",
    val addresses: List<Address> = emptyList(),
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val selectedAddress: Address? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
    val isOrderCreated: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)
