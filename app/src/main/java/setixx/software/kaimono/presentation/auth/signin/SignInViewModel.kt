package setixx.software.kaimono.presentation.auth.signin

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
import setixx.software.kaimono.domain.model.AuthResult
import setixx.software.kaimono.domain.usecase.SignInUseCase
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signInUseCase: SignInUseCase
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
        val password = _state.value.password

        var isValid = true

        if (email.isEmpty()) {
            _state.value = _state.value.copy(emailError = context.getString(R.string.error_email_empty))
            isValid = false
        } else if (!isValidEmail(email)) {
            _state.value = _state.value.copy(emailError = context.getString(R.string.error_email_format))
            isValid = false
        }

        if (password.isEmpty()) {
            _state.value = _state.value.copy(passwordError = context.getString(R.string.error_password_empty))
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

}