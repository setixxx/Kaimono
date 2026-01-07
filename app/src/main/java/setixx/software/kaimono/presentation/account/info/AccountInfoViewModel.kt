package setixx.software.kaimono.presentation.account.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Gender
import setixx.software.kaimono.domain.model.UserUpdate
import setixx.software.kaimono.domain.usecase.GetCurrentUserUseCase
import setixx.software.kaimono.domain.usecase.UpdateUserInfoUseCase
import setixx.software.kaimono.domain.validation.EmailValidator
import setixx.software.kaimono.domain.validation.NameValidator
import setixx.software.kaimono.domain.validation.PhoneValidator
import setixx.software.kaimono.domain.validation.ValidationResult
import setixx.software.kaimono.presentation.common.ErrorMapper
import setixx.software.kaimono.presentation.common.ValidationErrorMapper
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
            _state.value = _state.value.copy(isLoading = true)

            when (val result = getCurrentUserUseCase()) {
                is ApiResult.Success -> {
                    val user = result.data

                    _state.value = _state.value.copy(
                        name = user.name,
                        surname = user.surname ?: "Not specified",
                        phone = user.phone,
                        email = user.email,
                        dateOfBirth = user.birthday ?: "Not specified",
                        gender = user.gender.toString(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    fun updateUserInfo(){
        if (!validateInputs()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val request = UserUpdate(
                name = _state.value.name,
                surname = _state.value.surname,
                phone = _state.value.phone,
                email = _state.value.email,
                birthday = _state.value.dateOfBirth,
                gender = Gender.valueOf(_state.value.gender)
            )

            when (val result = updateUserInfoUseCase(request)){
                is ApiResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        name = data.name,
                        surname = data.surname ?: "Not specified",
                        phone = data.phone,
                        email = data.email,
                        dateOfBirth = data.birthday ?: "Not specified",
                        gender = data.gender.toString(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(
            name = name,
            nameError = null,
            errorMessage = null
        )
    }

    fun onSurnameChange(surname: String) {
        _state.value = _state.value.copy(
            surname = surname,
            surnameError = null,
            errorMessage = null
        )
    }

    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(
            phone = phone,
            phoneError = null,
            errorMessage = null
        )
    }

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(
            email = email,
            emailError = null,
            errorMessage = null
        )
    }

    fun onDateOfBirthChange(dateOfBirth: String) {
        _state.value = _state.value.copy(
            dateOfBirth = dateOfBirth,
            dateOfBirthError = null,
            errorMessage = null
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    private fun validateInputs(): Boolean {
        val nameResult = nameValidator.validateName(_state.value.name)
        val surnameResult = nameValidator.validateSurname(_state.value.surname)
        val phoneResult = phoneValidator.validate(_state.value.phone)
        val emailResult = emailValidator.validate(_state.value.email)

        var isValid = true

        if (nameResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                nameError = validationErrorMapper.mapToMessage(nameResult.error)
            )
            isValid = false
        }

        if (surnameResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                surnameError = validationErrorMapper.mapToMessage(surnameResult.error)
            )
            isValid = false
        }

        if (phoneResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                phoneError = validationErrorMapper.mapToMessage(phoneResult.error)
            )
            isValid = false
        }

        if (emailResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                emailError = validationErrorMapper.mapToMessage(emailResult.error)
            )
            isValid = false
        }

        return isValid
    }
}