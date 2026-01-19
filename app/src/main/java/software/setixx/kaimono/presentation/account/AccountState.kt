package software.setixx.kaimono.presentation.account

data class AccountState(
    val isLoggingOut: Boolean = false,
    val name: String = "",
    val nameError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isPaymentMethodsSheetOpen: Boolean = false,
    val isAddressesSheetOpen: Boolean = false
)