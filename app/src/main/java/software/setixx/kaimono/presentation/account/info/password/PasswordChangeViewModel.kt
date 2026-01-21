package software.setixx.kaimono.presentation.account.info.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.PasswordUpdate
import software.setixx.kaimono.domain.usecase.UpdatePasswordUseCase
import software.setixx.kaimono.domain.validation.PasswordValidator
import software.setixx.kaimono.domain.validation.ValidationResult
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.common.ValidationErrorMapper
import javax.inject.Inject

@HiltViewModel
class PasswordChangeViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val errorMapper: ErrorMapper,
    private val validationErrorMapper: ValidationErrorMapper,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordChangeState())
    val state: StateFlow<PasswordChangeState> = _state.asStateFlow()

    fun onCurrentPasswordChange(currentPassword: String) {
        _state.update {
            it.copy(
                currentPassword = currentPassword,
                currentPasswordError = null,
                errorMessage = null
            )
        }
    }

    fun onNewPasswordChange(newPassword: String) {
        _state.update {
            it.copy(
                newPassword = newPassword,
                newPasswordError = null,
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

    fun toggleOldPasswordVisibility() {
        _state.update {
            it.copy(isOldPasswordVisible = !it.isOldPasswordVisible)
        }
    }

    fun toggleNewPasswordVisibility() {
        _state.update {
            it.copy(isNewPasswordVisible = !it.isNewPasswordVisible)
        }
    }

    fun toggleConfirmPasswordVisibility() {
        _state.update {
            it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
        }
    }

    fun changePassword() {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val currentState = _state.value

            val request = PasswordUpdate(
                currentPassword = currentState.currentPassword,
                newPassword = currentState.newPassword,
                confirmPassword = currentState.confirmPassword
            )

            when (val result = updatePasswordUseCase(request)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
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

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun onSuccessShown() {
        _state.update { it.copy(isSuccess = false) }
    }

    private fun validateInput(): Boolean {
        val currentState = _state.value
        val newPasswordResult = passwordValidator.validate(currentState.newPassword)
        val confirmPasswordResult = passwordValidator.validateConfirmation(
            currentState.newPassword,
            currentState.confirmPassword
        )

        var isValid = true
        var newPasswordError: String? = null
        var confirmPasswordError: String? = null

        if (newPasswordResult is ValidationResult.Error) {
            newPasswordError = validationErrorMapper.mapToMessage(newPasswordResult.error)
            isValid = false
        }

        if (confirmPasswordResult is ValidationResult.Error) {
            confirmPasswordError = validationErrorMapper.mapToMessage(confirmPasswordResult.error)
            isValid = false
        }

        _state.update {
            it.copy(
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        return isValid
    }
}
