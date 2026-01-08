package setixx.software.kaimono.presentation.account

data class AccountState(
    val isLoggingOut: Boolean = false,
    val name: String = "",
    val nameError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    )