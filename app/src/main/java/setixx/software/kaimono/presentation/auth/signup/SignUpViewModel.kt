package setixx.software.kaimono.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.usecase.SignUpUseCase
import javax.inject.Inject
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.usecase.SignInUseCase
import setixx.software.kaimono.domain.validation.EmailValidator
import setixx.software.kaimono.domain.validation.PasswordValidator
import setixx.software.kaimono.domain.validation.PhoneValidator
import setixx.software.kaimono.domain.validation.ValidationResult
import setixx.software.kaimono.presentation.common.ErrorMapper
import setixx.software.kaimono.presentation.common.ValidationErrorMapper

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val errorMapper: ErrorMapper,
    private val validationErrorMapper: ValidationErrorMapper,
    private val emailValidator: EmailValidator,
    private val phoneValidator: PhoneValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(
            email = email,
            emailError = null,
            errorMessage = null
        )
    }

    fun onPhoneChange(phone: String){
        _state.value = _state.value.copy(
            phone = phone,
            phoneError = null,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(
            password = password,
            passwordError = null,
            errorMessage = null
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.value = _state.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null,
            errorMessage = null
        )
    }

    fun togglePasswordVisibility() {
        _state.value = _state.value.copy(
            isPasswordVisible = !_state.value.isPasswordVisible
        )
    }

    fun toggleConfirmPasswordVisibility() {
        _state.value = _state.value.copy(
            isConfirmPasswordVisible = !_state.value.isConfirmPasswordVisible
        )
    }

    fun onSignUpClick(onSuccess: () -> Unit) {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = signUpUseCase(
                email = _state.value.email.trim(),
                phone = _state.value.phone,
                password = _state.value.password
            )) {
                is ApiResult.Success -> {
                    val signInResult = signInUseCase(
                        email = _state.value.email.trim(),
                        password = _state.value.password
                    )
                    when (signInResult) {
                        is ApiResult.Success -> {
                            _state.value = _state.value.copy(isLoading = false)
                            onSuccess()
                        }
                        is ApiResult.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = errorMapper.mapToMessage(signInResult.error)
                            )
                        }
                        else -> {
                            _state.value = _state.value.copy(isLoading = false)
                        }
                    }
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                else -> {}
            }
        }
    }

    private fun validateInput(): Boolean {
        val emailResult = emailValidator.validate(_state.value.email)
        val phoneResult = phoneValidator.validate(_state.value.phone)
        val passwordResult = passwordValidator.validate(_state.value.password)
        val confirmPasswordResult = passwordValidator.validateConfirmation(
            _state.value.password,
            _state.value.confirmPassword
        )

        var isValid = true

        if (emailResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                emailError = validationErrorMapper.mapToMessage(emailResult.error)
            )
            isValid = false
        }

        if (phoneResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                phoneError = validationErrorMapper.mapToMessage(phoneResult.error)
            )
            isValid = false
        }

        if (passwordResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                passwordError = validationErrorMapper.mapToMessage(passwordResult.error)
            )
            isValid = false
        }

        if (confirmPasswordResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                confirmPasswordError = validationErrorMapper.mapToMessage(confirmPasswordResult.error)
            )
            isValid = false
        }

        return isValid
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}