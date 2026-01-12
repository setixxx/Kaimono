package setixx.software.kaimono.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateOrder
import setixx.software.kaimono.domain.usecase.ClearCartUseCase
import setixx.software.kaimono.domain.usecase.CreateOrderUseCase
import setixx.software.kaimono.domain.usecase.DeleteCartItemUseCase
import setixx.software.kaimono.domain.usecase.GetAddressesUseCase
import setixx.software.kaimono.domain.usecase.GetCartUseCase
import setixx.software.kaimono.domain.usecase.GetPaymentMethodsUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getAddressesUseCase: GetAddressesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {
    private val _state = MutableStateFlow(CartViewModelState())
    val state = _state.asStateFlow()

    init {
        loadCart()
        loadAddresses()
        loadPaymentMethods()
    }

    fun loadCart() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = getCartUseCase()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        cart = result.data,
                        items = result.data.items,
                        totalPrice = result.data.totalPrice,
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

    fun loadAddresses() {
        viewModelScope.launch {
            when (val result = getAddressesUseCase()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        addresses = result.data,
                        selectedAddress = result.data.find { it.isDefault } ?: result.data.firstOrNull()
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                else -> {}
            }
        }
    }

    fun loadPaymentMethods() {
        viewModelScope.launch {
            when (val result = getPaymentMethodsUseCase()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        paymentMethods = result.data,
                        selectedPaymentMethod = result.data.find { it.isDefault } ?: result.data.firstOrNull()
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                else -> {}
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = clearCartUseCase()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        cart = result.data,
                        items = result.data.items,
                        totalPrice = result.data.totalPrice,
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

    fun deleteCartItem(cartItemId: String, size: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = deleteCartItemUseCase(cartItemId, size)) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        cart = result.data,
                        items = result.data.items,
                        totalPrice = result.data.totalPrice,
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

    fun createOrder() {
        val selectedAddress = _state.value.selectedAddress
        if (selectedAddress == null) {
            _state.value = _state.value.copy(errorMessage = "Please select a delivery address")
            return
        }

        if (_state.value.items.isEmpty()) {
            _state.value = _state.value.copy(errorMessage = "Your cart is empty")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = createOrderUseCase(
                if (_state.value.selectedPaymentMethod?.paymentType == "card") {
                    CreateOrder(
                        addressId = selectedAddress.id,
                        paymentMethodId = _state.value.selectedPaymentMethod?.id,
                        paymentMethod = "card"
                    )
                } else {
                    CreateOrder(
                        addressId = selectedAddress.id,
                        paymentMethodId = _state.value.selectedPaymentMethod?.id,
                        paymentMethod = "cash"
                    )
                }
            )
            when (result) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isOrderCreated = true,
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

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun resetOrderCreated() {
        _state.value = _state.value.copy(isOrderCreated = false)
    }
}
