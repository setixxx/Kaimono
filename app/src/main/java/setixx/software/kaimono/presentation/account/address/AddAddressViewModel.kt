package setixx.software.kaimono.presentation.account.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateAddress
import setixx.software.kaimono.domain.usecase.AddAddressUseCase
import setixx.software.kaimono.domain.validation.AddressValidator
import setixx.software.kaimono.domain.validation.ValidationResult
import setixx.software.kaimono.presentation.common.ErrorMapper
import setixx.software.kaimono.presentation.common.ValidationErrorMapper
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val addAddressUseCase: AddAddressUseCase,
    private val errorMapper: ErrorMapper,
    private val addressValidator: AddressValidator,
    private val validationErrorMapper: ValidationErrorMapper
) : ViewModel() {
    private val _state = MutableStateFlow(AddAddressViewModelState())
    val state: StateFlow<AddAddressViewModelState> = _state.asStateFlow()

    fun addAddress() {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            val request = CreateAddress(
                city = _state.value.city,
                street = _state.value.street,
                house = _state.value.house,
                apartment = _state.value.apartment,
                zipCode = _state.value.zipCode,
                additionalInfo = _state.value.additionalInfo,
                isDefault = _state.value.isDefault
            )

            when (val result = addAddressUseCase(request)){
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

    fun onCityChange(city: String) {
        _state.value = _state.value.copy(
            city = city,
            cityError = null,
            errorMessage = null
        )
    }

    fun onStreetChange(street: String) {
        _state.value = _state.value.copy(
            street = street,
            streetError = null,
            errorMessage = null
        )
    }

    fun onHouseChange(house: String) {
        _state.value = _state.value.copy(
            house = house,
            houseError = null,
            errorMessage = null
        )
    }

    fun onApartmentChange(apartment: String) {
        _state.value = _state.value.copy(
            apartment = apartment,
            apartmentError = null,
            errorMessage = null
        )
    }

    fun onZipCodeChange(zipCode: String) {
        _state.value = _state.value.copy(
            zipCode = zipCode,
            zipCodeError = null,
            errorMessage = null
        )
    }

    fun onAdditionalInfoChange(additionalInfo: String) {
        _state.value = _state.value.copy(
            additionalInfo = additionalInfo,
            additionalInfoError = null,
            errorMessage = null
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    private fun validateInput(): Boolean {
        val cityResult = addressValidator.validateCity(_state.value.city)
        val streetResult = addressValidator.validateStreet(_state.value.street)
        val houseResult = addressValidator.validateHouse(_state.value.house)
        val apartmentResult = addressValidator.validateApartment(_state.value.apartment)
        val zipCodeResult = addressValidator.validateZipCode(_state.value.zipCode)

        val hasError = cityResult is ValidationResult.Error ||
                streetResult is ValidationResult.Error ||
                houseResult is ValidationResult.Error ||
                apartmentResult is ValidationResult.Error ||
                zipCodeResult is ValidationResult.Error

        if (hasError) {
            _state.value = _state.value.copy(
                cityError = if (cityResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cityResult.error) else null,
                streetError = if (streetResult is ValidationResult.Error) validationErrorMapper.mapToMessage(streetResult.error) else null,
                houseError = if (houseResult is ValidationResult.Error) validationErrorMapper.mapToMessage(houseResult.error) else null,
                apartmentError = if (apartmentResult is ValidationResult.Error) validationErrorMapper.mapToMessage(apartmentResult.error) else null,
                zipCodeError = if (zipCodeResult is ValidationResult.Error) validationErrorMapper.mapToMessage(zipCodeResult.error) else null
            )
            return false
        }
        return true
    }
}