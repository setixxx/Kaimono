package software.setixx.kaimono.presentation.account.orders

import software.setixx.kaimono.domain.model.Order

data class AccountOrdersViewModelState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
