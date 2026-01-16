package software.setixx.kaimono.presentation.account.paymnetmethod

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreatePaymentMethod
import software.setixx.kaimono.domain.usecase.AddPaymentMethodUseCase
import software.setixx.kaimono.domain.validation.PaymentMethodValidator
import software.setixx.kaimono.domain.validation.ValidationResult
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.common.ValidationErrorMapper
import javax.inject.Inject

@HiltViewModel
class AddPaymentMethodViewModel @Inject constructor(
    private val addPaymentMethodUseCase: AddPaymentMethodUseCase,
    private val validator: PaymentMethodValidator,
    private val errorMapper: ErrorMapper,
    private val validationErrorMapper: ValidationErrorMapper
) : ViewModel() {

    private val _state = MutableStateFlow(AddPaymentMethodViewModelState())
    val state: StateFlow<AddPaymentMethodViewModelState> = _state.asStateFlow()

    fun onCardNumberChange(cardNumber: String) {
        val digitsOnly = cardNumber.filter { it.isDigit() }
        if (digitsOnly.length > 16) return

        _state.value = _state.value.copy(
            cardNumber = digitsOnly,
            cardNumberError = null,
            errorMessage = null
        )
        Log.d("Card", _state.value.cardNumber)
    }

    fun onCardHolderNameChange(name: String) {
        _state.value = _state.value.copy(
            cardHolderName = name,
            cardHolderNameError = null,
            errorMessage = null
        )
    }

    fun onExpiryDateChange(expiryDate: String) {
        val digitsOnly = expiryDate.filter { it.isDigit() }
        if (digitsOnly.length > 4) return

        _state.value = _state.value.copy(
            expiryDate = digitsOnly,
            expiryDateError = null,
            errorMessage = null
        )
    }

    fun onCvvChange(cvv: String) {
        val digitsOnly = cvv.filter { it.isDigit() }
        if (digitsOnly.length > 3) return

        _state.value = _state.value.copy(
            cvv = digitsOnly,
            cvvError = null,
            errorMessage = null
        )
    }

    fun savePaymentMethod() {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            val month = _state.value.expiryDate.substring(0, 2).toShort()

            val yearInput = _state.value.expiryDate.substring(2, 4)
            val fullYear = "20$yearInput".toShort()

            val request = CreatePaymentMethod(
                cardNumber = _state.value.cardNumber,
                cardHolderName = _state.value.cardHolderName,
                expiryMonth = month,
                expiryYear = fullYear,
                cvv = _state.value.cvv,
                isDefault = _state.value.isDefault
            )

            when (val result = addPaymentMethodUseCase(request)) {
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
        val cardNumberResult = validator.validateCardNumber(_state.value.cardNumber)
        val cardHolderResult = validator.validateCardHolderName(_state.value.cardHolderName)
        val expiryDateResult = validator.validateExpiryDate(_state.value.expiryDate)
        val cvvResult = validator.validateCvv(_state.value.cvv)

        val hasError = cardNumberResult is ValidationResult.Error ||
                cardHolderResult is ValidationResult.Error ||
                expiryDateResult is ValidationResult.Error ||
                cvvResult is ValidationResult.Error

        if (hasError) {
            _state.value = _state.value.copy(
                cardNumberError = if (cardNumberResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cardNumberResult.error) else null,
                cardHolderNameError = if (cardHolderResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cardHolderResult.error) else null,
                expiryDateError = if (expiryDateResult is ValidationResult.Error) validationErrorMapper.mapToMessage(expiryDateResult.error) else null,
                cvvError = if (cvvResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cvvResult.error) else null
            )
            return false
        }
        return true
    }
}
