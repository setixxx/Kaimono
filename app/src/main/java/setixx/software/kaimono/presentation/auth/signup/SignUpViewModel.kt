package setixx.software.kaimono.presentation.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.R
import setixx.software.kaimono.domain.usecase.SignUpUseCase
import javax.inject.Inject
import setixx.software.kaimono.domain.model.AuthResult

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signUpUseCase: SignUpUseCase
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
                is AuthResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                else -> {}
            }
        }
    }

    private fun validateInput(): Boolean {
        val email = _state.value.email.trim()
        val phone = _state.value.phone
        val password = _state.value.password
        val confirmPassword = _state.value.confirmPassword

        var isValid = true

        if (email.isEmpty()) {
            _state.value = _state.value.copy(
                emailError = context.getString(R.string.error_email_empty)
            )
            isValid = false
        } else if (!isValidEmail(email)) {
            _state.value = _state.value.copy(
                emailError = context.getString(R.string.error_email_format)
            )
            isValid = false
        }

        if (phone.isEmpty()){
            _state.value = _state.value.copy(
                phoneError = context.getString(R.string.error_phone_empty)
            )
            isValid = false
        } else if (phone.length < 11) {
            _state.value = _state.value.copy(
                phoneError = context.getString(R.string.error_phone_format)
            )
            isValid = false
        }

        if (password.isEmpty()) {
            _state.value = _state.value.copy(
                passwordError = context.getString(R.string.error_password_empty)
            )
            isValid = false
        } else if (password.length < 8) {
            _state.value = _state.value.copy(
                passwordError = context.getString(R.string.error_password_length)
            )
            isValid = false
        } else if (!isValidPassword(password)) {
            _state.value = _state.value.copy(
                passwordError = context.getString(R.string.error_password_complexity)
            )
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            _state.value = _state.value.copy(
                confirmPasswordError = context.getString(R.string.error_confirm_password_empty)
            )
            isValid = false
        } else if (password != confirmPassword) {
            _state.value = _state.value.copy(
                confirmPasswordError = context.getString(R.string.error_passwords_mismatch)
            )
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }

    private fun isValidPassword(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        return hasUpperCase && hasLowerCase && hasDigit
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}