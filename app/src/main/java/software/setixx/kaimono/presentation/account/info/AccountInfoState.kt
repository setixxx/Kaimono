package software.setixx.kaimono.presentation.account.info

data class AccountInfoState(
    val name: String = "",
    val surname: String = "",
    val phone: String = "",
    val email: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val nameError: String? = null,
    val surnameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val dateOfBirthError: String? = null,
    val isSurnameChanged: Boolean = false,
    val isBirthdateChanged: Boolean = false,
    val isFieldsChanged: Boolean = false
)