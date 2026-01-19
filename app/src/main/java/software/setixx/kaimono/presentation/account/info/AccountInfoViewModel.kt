package software.setixx.kaimono.presentation.account.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.Gender
import software.setixx.kaimono.domain.model.UserUpdate
import software.setixx.kaimono.domain.usecase.GetCurrentUserUseCase
import software.setixx.kaimono.domain.usecase.UpdateUserInfoUseCase
import software.setixx.kaimono.domain.validation.EmailValidator
import software.setixx.kaimono.domain.validation.NameValidator
import software.setixx.kaimono.domain.validation.PhoneValidator
import software.setixx.kaimono.domain.validation.ValidationResult
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.common.ValidationErrorMapper
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val errorMapper: ErrorMapper,
    private val validationErrorMapper: ValidationErrorMapper,
    private val emailValidator: EmailValidator,
    private val phoneValidator: PhoneValidator,
    private val nameValidator: NameValidator
) : ViewModel() {

    private val _state = MutableStateFlow(AccountInfoState())
    val state: StateFlow<AccountInfoState> = _state.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getCurrentUserUseCase()) {
                is ApiResult.Success -> {
                    val user = result.data

                    _state.update {
                        it.copy(
                            name = user.name,
                            surname = user.surname ?: "",
                            phone = user.phone,
                            email = user.email,
                            dateOfBirth = user.birthday ?: "",
                            gender = user.gender.toString(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }

                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun updateUserInfo() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentState = _state.value
            val request = UserUpdate(
                name = currentState.name,
                surname = if (currentState.isSurnameChanged || currentState.surname.isNotEmpty()) currentState.surname else null,
                phone = currentState.phone,
                email = currentState.email,
                birthday = if (currentState.isBirthdateChanged || currentState.dateOfBirth.isNotEmpty()) currentState.dateOfBirth else null,
                gender = Gender.valueOf(currentState.gender)
            )

            when (val result = updateUserInfoUseCase(request)) {
                is ApiResult.Success -> {
                    val data = result.data

                    _state.update {
                        it.copy(
                            name = data.name,
                            surname = data.surname ?: "",
                            phone = data.phone,
                            email = data.email,
                            dateOfBirth = data.birthday ?: "",
                            gender = data.gender.toString(),
                            isLoading = false,
                            errorMessage = null,
                            isFieldsChanged = false
                        )
                    }
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }

                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name,
                nameError = null,
                errorMessage = null,
                isFieldsChanged = true
            )
        }
    }

    fun onSurnameChange(surname: String) {
        _state.update {
            it.copy(
                surname = surname,
                surnameError = null,
                errorMessage = null,
                isSurnameChanged = true,
                isFieldsChanged = true
            )
        }
    }

    fun onPhoneChange(phone: String) {
        _state.update {
            it.copy(
                phone = phone,
                phoneError = null,
                errorMessage = null,
                isFieldsChanged = true
            )
        }
    }

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null,
                isFieldsChanged = true
            )
        }
    }

    fun onDateOfBirthChange(dateOfBirth: String) {
        _state.update {
            it.copy(
                dateOfBirth = dateOfBirth,
                dateOfBirthError = null,
                errorMessage = null,
                isBirthdateChanged = true,
                isFieldsChanged = true
            )
        }
    }

    fun onGenderChange(gender: String) {
        _state.update {
            it.copy(
                gender = gender,
                errorMessage = null,
                isFieldsChanged = true
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun validateInputs(): Boolean {
        val currentState = _state.value
        val nameResult = nameValidator.validateName(currentState.name)
        val surnameResult = nameValidator.validateSurname(currentState.surname)
        val phoneResult = phoneValidator.validate(currentState.phone)
        val emailResult = emailValidator.validate(currentState.email)

        var isValid = true
        var nameError: String? = null
        var surnameError: String? = null
        var phoneError: String? = null
        var emailError: String? = null

        if (nameResult is ValidationResult.Error) {
            nameError = validationErrorMapper.mapToMessage(nameResult.error)
            isValid = false
        }

        if (surnameResult is ValidationResult.Error && currentState.isSurnameChanged) {
            surnameError = validationErrorMapper.mapToMessage(surnameResult.error)
            isValid = false
        }

        if (phoneResult is ValidationResult.Error) {
            phoneError = validationErrorMapper.mapToMessage(phoneResult.error)
            isValid = false
        }

        if (emailResult is ValidationResult.Error) {
            emailError = validationErrorMapper.mapToMessage(emailResult.error)
            isValid = false
        }

        _state.update {
            it.copy(
                nameError = nameError,
                surnameError = surnameError,
                phoneError = phoneError,
                emailError = emailError
            )
        }

        return isValid
    }
}
