package setixx.software.kaimono.presentation.account.paymnetmethod

data class AddPaymentMethodViewModelState(
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isDefault: Boolean = true,
    
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,

    val cardNumberError: String? = null,
    val cardHolderNameError: String? = null,
    val expiryDateError: String? = null,
    val cvvError: String? = null
)
