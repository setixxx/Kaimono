package software.setixx.kaimono.presentation.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        _state.value = _state.value.copy(
            email = email,
            emailError = null,
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

    fun togglePasswordVisibility() {
        _state.value = _state.value.copy(
            isPasswordVisible = !_state.value.isPasswordVisible
        )
    }

    fun onSignInClick(onSuccess: () -> Unit) {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = signInUseCase(
                email = _state.value.email.trim(),
                password = _state.value.password,
            )) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    onSuccess()
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
        val passwordResult = passwordValidator.validate(_state.value.password)

        var isValid = true

        if (emailResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                emailError = validationErrorMapper.mapToMessage(emailResult.error)
            )
            isValid = false
        }

        if (passwordResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                passwordError = validationErrorMapper.mapToMessage(passwordResult.error)
            )
            isValid = false
        }

        return isValid
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}