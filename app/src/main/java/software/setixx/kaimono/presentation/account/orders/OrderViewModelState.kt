package software.setixx.kaimono.presentation.account.orders

import software.setixx.kaimono.domain.model.Order
import software.setixx.kaimono.domain.model.OrderItem

data class OrderViewModelState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val writtenProductReviews: Map<OrderItem, Boolean> = emptyMap()
)
