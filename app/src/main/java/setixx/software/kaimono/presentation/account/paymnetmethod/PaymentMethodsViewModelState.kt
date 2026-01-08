package setixx.software.kaimono.presentation.account.paymnetmethod

import setixx.software.kaimono.domain.model.PaymentMethod

data class PaymentMethodsViewModelState(
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val selectedPaymentMethod: PaymentMethod? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
