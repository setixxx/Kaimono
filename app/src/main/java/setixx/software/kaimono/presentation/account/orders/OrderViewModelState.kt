package setixx.software.kaimono.presentation.account.orders

import setixx.software.kaimono.domain.model.Order

data class OrderViewModelState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
