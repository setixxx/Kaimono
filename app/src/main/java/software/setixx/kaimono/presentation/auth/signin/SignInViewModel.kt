package software.setixx.kaimono.presentation.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.usecase.SignInUseCase
import software.setixx.kaimono.domain.validation.EmailValidator
import software.setixx.kaimono.domain.validation.PasswordValidator
import software.setixx.kaimono.domain.validation.ValidationResult
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.common.ValidationErrorMapper
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val errorMapper: ErrorMapper,
    private val validationErrorMapper: ValidationErrorMapper,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                emailError = null,
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

    fun togglePasswordVisibility() {
        _state.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    fun onSignInClick(onSuccess: () -> Unit) {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val currentState = _state.value

            when (val result = signInUseCase(
                email = currentState.email.trim(),
                password = currentState.password,
            )) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    onSuccess()
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
        val passwordResult = passwordValidator.validate(currentState.password)

        var isValid = true
        var emailError: String? = null
        var passwordError: String? = null

        if (emailResult is ValidationResult.Error) {
            emailError = validationErrorMapper.mapToMessage(emailResult.error)
            isValid = false
        }

        if (passwordResult is ValidationResult.Error) {
            passwordError = validationErrorMapper.mapToMessage(passwordResult.error)
            isValid = false
        }

        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }

        return isValid
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
