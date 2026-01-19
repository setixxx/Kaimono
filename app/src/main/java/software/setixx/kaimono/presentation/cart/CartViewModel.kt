package software.setixx.kaimono.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateOrder
import software.setixx.kaimono.domain.usecase.ClearCartUseCase
import software.setixx.kaimono.domain.usecase.CreateOrderUseCase
import software.setixx.kaimono.domain.usecase.DeleteCartItemUseCase
import software.setixx.kaimono.domain.usecase.GetAddressesUseCase
import software.setixx.kaimono.domain.usecase.GetCartUseCase
import software.setixx.kaimono.domain.usecase.GetPaymentMethodsUseCase
import software.setixx.kaimono.domain.validation.AddressValidator
import software.setixx.kaimono.domain.validation.CartValidator
import software.setixx.kaimono.domain.validation.ValidationResult
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.common.ValidationErrorMapper
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getAddressesUseCase: GetAddressesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val errorMapper: ErrorMapper,
    private val addressValidator: AddressValidator,
    private val cartValidator: CartValidator,
    private val validationErrorMapper: ValidationErrorMapper
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
            _state.update { it.copy(isLoading = true) }
            when (val result = getCartUseCase()) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            cart = result.data,
                            items = result.data.items,
                            totalPrice = result.data.totalPrice,
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

    fun loadAddresses() {
        viewModelScope.launch {
            when (val result = getAddressesUseCase()) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            addresses = result.data,
                            selectedAddress = result.data.find { it.isDefault }
                                ?: result.data.firstOrNull()
                        )
                    }
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
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

    fun loadPaymentMethods() {
        viewModelScope.launch {
            when (val result = getPaymentMethodsUseCase()) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            paymentMethods = result.data,
                            selectedPaymentMethod = result.data.find { it.isDefault }
                                ?: result.data.firstOrNull()
                        )
                    }
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
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

    fun clearCart() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = clearCartUseCase()) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            cart = result.data,
                            items = result.data.items,
                            totalPrice = result.data.totalPrice,
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

    fun deleteCartItem(cartItemId: String, size: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = deleteCartItemUseCase(cartItemId, size)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            cart = result.data,
                            items = result.data.items,
                            totalPrice = result.data.totalPrice,
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

    fun createOrder() {
        val currentState = _state.value
        if (!validate()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val createOrder = if (currentState.selectedPaymentMethod?.paymentType == "card") {
                CreateOrder(
                    addressId = currentState.selectedAddress!!.id,
                    paymentMethodId = currentState.selectedPaymentMethod.id,
                    paymentMethod = "card"
                )
            } else {
                CreateOrder(
                    addressId = currentState.selectedAddress!!.id,
                    paymentMethodId = currentState.selectedPaymentMethod?.id,
                    paymentMethod = "cash"
                )
            }

            when (val result = createOrderUseCase(createOrder)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isOrderCreated = true,
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

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun onIsAddressesSheetOpen(value: Boolean) = _state.update { it.copy(isAddressesSheetOpen = value) }

    fun onIsPaymentMethodsSheetOpen(value: Boolean) =
        _state.update { it.copy(isPaymentMethodsSheetOpen = value) }

    fun onIsDialogOpen(value: Boolean) = _state.update { it.copy(isDialogOpen = value) }

    fun resetOrderCreated() {
        _state.update { it.copy(isOrderCreated = false) }
    }

    private fun validate(): Boolean {
        val currentState = _state.value
        val addressResult = addressValidator.validateAddress(currentState.selectedAddress)
        val cartResult = cartValidator.validateCart(currentState.cart)
        val addressError = if (addressResult is ValidationResult.Error) validationErrorMapper.mapToMessage(addressResult.error) else null
        val cartError = if (cartResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cartResult.error) else null
        val hasError = addressError != null || cartError != null
        if (hasError) {
            _state.update {
                it.copy(
                    errorMessage = addressError
                )
            }
            return false
        }
        return true
    }
}
