package setixx.software.kaimono.presentation.account.info

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.AuthResult
import setixx.software.kaimono.domain.repository.UserRepository
import setixx.software.kaimono.domain.usecase.GetCurrentUserUseCase
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
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
                is AuthResult.Success -> {
                    val user = result.data

                    _state.value = _state.value.copy(
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
                is AuthResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                    )
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
}