package software.setixx.kaimono.presentation.account.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.usecase.DeleteAddressUseCase
import software.setixx.kaimono.domain.usecase.GetAddressesUseCase
import software.setixx.kaimono.domain.usecase.SetDefaultAddressUseCase
import software.setixx.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val setDefaultAddressUseCase: SetDefaultAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val errorMapper: ErrorMapper
): ViewModel() {
    private val _state = MutableStateFlow(AddressViewModelState())
    val state: StateFlow<AddressViewModelState> = _state.asStateFlow()

    init {
        loadAddresses()
    }

    fun loadAddresses() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = getAddressesUseCase()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        addresses = result.data,
                        isLoading = false,
                        errorMessage = null,
                        selectedAddress = result.data.find { it.isDefault }
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

    fun setDefaultAddress(addressId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = setDefaultAddressUseCase(addressId)) {
                is ApiResult.Success -> {
                    loadAddresses()
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error),
                    )
                    loadAddresses()
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    fun deleteAddress(addressId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = deleteAddressUseCase(addressId)) {
                is ApiResult.Success -> {
                    loadAddresses()
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
}