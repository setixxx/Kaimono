package software.setixx.kaimono.presentation.account.paymnetmethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

        _state.update {
            it.copy(
                cardNumber = digitsOnly,
                cardNumberError = null,
                errorMessage = null
            )
        }
    }

    fun onCardHolderNameChange(name: String) {
        _state.update {
            it.copy(
                cardHolderName = name,
                cardHolderNameError = null,
                errorMessage = null
            )
        }
    }

    fun onExpiryDateChange(expiryDate: String) {
        val digitsOnly = expiryDate.filter { it.isDigit() }
        if (digitsOnly.length > 4) return

        _state.update {
            it.copy(
                expiryDate = digitsOnly,
                expiryDateError = null,
                errorMessage = null
            )
        }
    }

    fun onCvvChange(cvv: String) {
        val digitsOnly = cvv.filter { it.isDigit() }
        if (digitsOnly.length > 3) return

        _state.update {
            it.copy(
                cvv = digitsOnly,
                cvvError = null,
                errorMessage = null
            )
        }
    }

    fun savePaymentMethod() {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val currentState = _state.value

            val month = currentState.expiryDate.substring(0, 2).toShort()

            val yearInput = currentState.expiryDate.substring(2, 4)
            val fullYear = "20$yearInput".toShort()

            val request = CreatePaymentMethod(
                cardNumber = currentState.cardNumber,
                cardHolderName = currentState.cardHolderName,
                expiryMonth = month,
                expiryYear = fullYear,
                cvv = currentState.cvv,
                isDefault = currentState.isDefault
            )

            when (val result = addPaymentMethodUseCase(request)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
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

    fun onSuccessShown() {
        _state.update { it.copy(isSuccess = false) }
    }

    private fun validateInput(): Boolean {
        val currentState = _state.value
        val cardNumberResult = validator.validateCardNumber(currentState.cardNumber)
        val cardHolderResult = validator.validateCardHolderName(currentState.cardHolderName)
        val expiryDateResult = validator.validateExpiryDate(currentState.expiryDate)
        val cvvResult = validator.validateCvv(currentState.cvv)

        val cardNumberError = if (cardNumberResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cardNumberResult.error) else null
        val cardHolderNameError = if (cardHolderResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cardHolderResult.error) else null
        val expiryDateError = if (expiryDateResult is ValidationResult.Error) validationErrorMapper.mapToMessage(expiryDateResult.error) else null
        val cvvError = if (cvvResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cvvResult.error) else null

        val hasError = cardNumberError != null ||
                cardHolderNameError != null ||
                expiryDateError != null ||
                cvvError != null

        if (hasError) {
            _state.update {
                it.copy(
                    cardNumberError = cardNumberError,
                    cardHolderNameError = cardHolderNameError,
                    expiryDateError = expiryDateError,
                    cvvError = cvvError
                )
            }
            return false
        }
        return true
    }
}
