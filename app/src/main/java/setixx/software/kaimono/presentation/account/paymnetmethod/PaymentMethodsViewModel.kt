package setixx.software.kaimono.presentation.account.paymnetmethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.usecase.DeletePaymentMethodUseCase
import setixx.software.kaimono.domain.usecase.GetPaymentMethodsUseCase
import setixx.software.kaimono.domain.usecase.SetDefaultPaymentMethodUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class PaymentMethodsViewModel @Inject constructor(
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val setDefaultPaymentMethodUseCase: SetDefaultPaymentMethodUseCase,
    private val deletePaymentMethodUseCase: DeletePaymentMethodUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentMethodsViewModelState())
    val state: StateFlow<PaymentMethodsViewModelState> = _state.asStateFlow()

    init {
        loadPaymentMethods()
    }

    fun loadPaymentMethods() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = getPaymentMethodsUseCase()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        paymentMethods = result.data,
                        selectedPaymentMethod = result.data.find { it.isDefault },
                        errorMessage = null
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                is ApiResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    fun setDefaultPaymentMethod(paymentMethodId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = setDefaultPaymentMethodUseCase(paymentMethodId)) {
                is ApiResult.Success -> {
                    loadPaymentMethods()
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                    loadPaymentMethods()
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    fun deletePaymentMethod(paymentMethodId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = deletePaymentMethodUseCase(paymentMethodId)) {
                is ApiResult.Success -> {
                    loadPaymentMethods()
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
