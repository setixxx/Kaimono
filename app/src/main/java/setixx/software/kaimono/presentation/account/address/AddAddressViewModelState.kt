package setixx.software.kaimono.presentation.account.address

data class AddAddressViewModelState (
    val city: String = "",
    val street: String = "",
    val house: String = "",
    val apartment: String = "",
    val zipCode: String = "",
    val additionalInfo: String = "",
    val isDefault: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val cityError: String? = null,
    val streetError: String? = null,
    val houseError: String? = null,
    val apartmentError: String? = null,
    val zipCodeError: String? = null,
    val additionalInfoError: String? = null,
    val isSuccess: Boolean = false
)