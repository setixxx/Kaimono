package software.setixx.kaimono.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.usecase.GetCurrentUserUseCase
import software.setixx.kaimono.domain.usecase.LogoutUseCase
import software.setixx.kaimono.presentation.common.ErrorMapper
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
            _state.update { it.copy(isLoading = true) }

            when (val result = getCurrentUserUseCase()) {
                is ApiResult.Success -> {
                    val user = result.data

                    _state.update {
                        it.copy(
                            name = user.name,
                            isLoading = false,
                            errorMessage = null
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

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingOut = true) }

            when (logoutUseCase()) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isLoggingOut = false) }
                    onSuccess()
                }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoggingOut = false) }
                    onSuccess()
                }
                else -> {
                    _state.update { it.copy(isLoggingOut = false) }
                }
            }
        }
    }

    fun onIsPaymentMethodsSheetOpen(value: Boolean){
        _state.update { it.copy(isPaymentMethodsSheetOpen = value) }
    }

    fun onIsAddressesSheetOpen(value: Boolean) {
        _state.update { it.copy(isAddressesSheetOpen = value) }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}