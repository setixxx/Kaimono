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
import setixx.software.kaimono.R
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Gender
import setixx.software.kaimono.domain.model.UserUpdate
import setixx.software.kaimono.domain.usecase.GetCurrentUserUseCase
import setixx.software.kaimono.domain.usecase.UpdateUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
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
                        surname = user.surname!!,
                        phone = user.phone,
                        email = user.email,
                        dateOfBirth = user.birthday!!,
                        gender = user.gender.toString(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is ApiResult.Error -> {
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

    private fun updateUserInfo(){
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
                        surname = data.surname!!,
                        phone = data.phone,
                        email = data.email,
                        dateOfBirth = data.birthday!!,
                        gender = data.gender.toString(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is ApiResult.Error -> {
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

    private fun validateInputs(): Boolean {
        val name = _state.value.name.trim()
        val surname = _state.value.surname.trim()
        val phone = _state.value.phone.trim()
        val email = _state.value.email.trim()

        var isValid = true

        if (name.isBlank()) {
            _state.value = _state.value.copy(
                nameError = context.getString(R.string.error_name_empty)
            )
            isValid = false
        } else if (!isValidNameOrSurname(name)) {
            _state.value = _state.value.copy(
                nameError = context.getString(R.string.error_name_format)
            )
            isValid = false
        }

        if (surname.isBlank()) {
            _state.value = _state.value.copy(
                surnameError = context.getString(R.string.error_surname_empty)
            )
            isValid = false
        } else if (!isValidNameOrSurname(surname)) {
            _state.value = _state.value.copy(
                surnameError = context.getString(R.string.error_surname_format)
            )
            isValid = false
        }

        if (phone.isBlank()) {
            _state.value = _state.value.copy(
                phoneError = context.getString(R.string.error_phone_empty)
            )
            isValid = false
        } else if (!isPhoneValid(phone)) {
            _state.value = _state.value.copy(
                phoneError = context.getString(R.string.error_phone_format)
            )
            isValid = false
        }

        if (email.isBlank()) {
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
        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }

    private fun isValidNameOrSurname(nameOrSurname: String): Boolean {
        return nameOrSurname.matches(Regex("^[a-zA-Zа-яА-Я]+$"))
    }

    private fun isPhoneValid(phone: String): Boolean {
        return phone.matches(Regex("^\\d{10}$"))
    }
}