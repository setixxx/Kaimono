package software.setixx.kaimono.presentation.account.info.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        _state.value = _state.value.copy(
            currentPassword = currentPassword,
            currentPasswordError = null,
            errorMessage = null
        )
    }
    fun onNewPasswordChange(newPassword: String) {
        _state.value = _state.value.copy(
            newPassword = newPassword,
            newPasswordError = null,
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

    fun toggleOldPasswordVisibility() {
        _state.value = _state.value.copy(
            isOldPasswordVisible = !_state.value.isOldPasswordVisible
        )
    }

    fun toggleNewPasswordVisibility() {
        _state.value = _state.value.copy(
            isNewPasswordVisible = !_state.value.isNewPasswordVisible
        )
    }

    fun toggleConfirmPasswordVisibility() {
        _state.value = _state.value.copy(
            isConfirmPasswordVisible = !_state.value.isConfirmPasswordVisible
        )
    }

    fun changePassword() {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            val request = PasswordUpdate(
                currentPassword = _state.value.currentPassword,
                newPassword = _state.value.newPassword,
                confirmPassword = _state.value.confirmPassword
            )

            when (val result = updatePasswordUseCase(request)) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccess = true
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

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    private fun validateInput(): Boolean {
        val newPasswordResult = passwordValidator.validate(_state.value.newPassword)
        val confirmPasswordResult = passwordValidator.validateConfirmation(
            _state.value.newPassword,
            _state.value.confirmPassword
        )

        var isValid = true

        if (newPasswordResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                newPasswordError = validationErrorMapper.mapToMessage(newPasswordResult.error)
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
}