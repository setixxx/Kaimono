package setixx.software.kaimono.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.usecase.GetCurrentUserUseCase
import setixx.software.kaimono.domain.usecase.LogoutUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val errorMapper: ErrorMapper,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

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
                        isLoading = false,
                        errorMessage = null
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

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoggingOut = true)

            when (logoutUseCase()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(isLoggingOut = false)
                    onSuccess()
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(isLoggingOut = false)
                    onSuccess()
                }
                else -> {
                    _state.value = _state.value.copy(isLoggingOut = false)
                }
            }
        }
    }
}