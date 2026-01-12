package setixx.software.kaimono.presentation.account.orders

import setixx.software.kaimono.domain.model.Order

data class AccountOrdersViewModelState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
