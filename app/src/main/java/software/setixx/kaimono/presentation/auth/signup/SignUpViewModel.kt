package software.setixx.kaimono.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.usecase.SignUpUseCase
import javax.inject.Inject
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.usecase.SignInUseCase
import software.setixx.kaimono.domain.validation.EmailValidator
import software.setixx.kaimono.domain.validation.PasswordValidator
import software.setixx.kaimono.domain.validation.PhoneValidator
import software.setixx.kaimono.domain.validation.ValidationResult
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.common.ValidationErrorMapper

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
        _state.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null
            )
        }
    }

    fun onPhoneChange(phone: String) {
        val digitsOnly = phone.filter { it.isDigit() }
        if (digitsOnly.length > 11) return

        _state.update {
            it.copy(
                phone = phone,
                phoneError = null,
                errorMessage = null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordError = null,
                errorMessage = null
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = null,
                errorMessage = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _state.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    fun toggleConfirmPasswordVisibility() {
        _state.update {
            it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
        }
    }

    fun onSignUpClick(onSuccess: () -> Unit) {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val currentState = _state.value

            when (val result = signUpUseCase(
                email = currentState.email.trim(),
                phone = currentState.phone,
                password = currentState.password
            )) {
                is ApiResult.Success -> {
                    val signInResult = signInUseCase(
                        email = currentState.email.trim(),
                        password = currentState.password
                    )
                    when (signInResult) {
                        is ApiResult.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            onSuccess()
                        }

                        is ApiResult.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = errorMapper.mapToMessage(signInResult.error)
                                )
                            }
                        }

                        else -> {
                            _state.update { it.copy(isLoading = false) }
                        }
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

    private fun validateInput(): Boolean {
        val currentState = _state.value
        val emailResult = emailValidator.validate(currentState.email)
        val phoneResult = phoneValidator.validate(currentState.phone)
        val passwordResult = passwordValidator.validate(currentState.password)
        val confirmPasswordResult = passwordValidator.validateConfirmation(
            currentState.password,
            currentState.confirmPassword
        )

        var isValid = true
        var emailError: String? = null
        var phoneError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        if (emailResult is ValidationResult.Error) {
            emailError = validationErrorMapper.mapToMessage(emailResult.error)
            isValid = false
        }

        if (phoneResult is ValidationResult.Error) {
            phoneError = validationErrorMapper.mapToMessage(phoneResult.error)
            isValid = false
        }

        if (passwordResult is ValidationResult.Error) {
            passwordError = validationErrorMapper.mapToMessage(passwordResult.error)
            isValid = false
        }

        if (confirmPasswordResult is ValidationResult.Error) {
            confirmPasswordError = validationErrorMapper.mapToMessage(confirmPasswordResult.error)
            isValid = false
        }

        _state.update {
            it.copy(
                emailError = emailError,
                phoneError = phoneError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        return isValid
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
