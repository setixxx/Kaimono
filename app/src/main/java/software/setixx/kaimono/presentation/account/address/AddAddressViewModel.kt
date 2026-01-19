package software.setixx.kaimono.presentation.account.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateAddress
import software.setixx.kaimono.domain.usecase.AddAddressUseCase
import software.setixx.kaimono.domain.validation.AddressValidator
import software.setixx.kaimono.domain.validation.ValidationResult
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.common.ValidationErrorMapper
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
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val currentState = _state.value

            val request = CreateAddress(
                city = currentState.city,
                street = currentState.street,
                house = currentState.house,
                apartment = currentState.apartment,
                zipCode = currentState.zipCode,
                additionalInfo = currentState.additionalInfo,
                isDefault = currentState.isDefault
            )

            when (val result = addAddressUseCase(request)) {
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

    fun onCityChange(city: String) {
        _state.update {
            it.copy(
                city = city,
                cityError = null,
                errorMessage = null
            )
        }
    }

    fun onStreetChange(street: String) {
        _state.update {
            it.copy(
                street = street,
                streetError = null,
                errorMessage = null
            )
        }
    }

    fun onHouseChange(house: String) {
        _state.update {
            it.copy(
                house = house,
                houseError = null,
                errorMessage = null
            )
        }
    }

    fun onApartmentChange(apartment: String) {
        _state.update {
            it.copy(
                apartment = apartment,
                apartmentError = null,
                errorMessage = null
            )
        }
    }

    fun onZipCodeChange(zipCode: String) {
        _state.update {
            it.copy(
                zipCode = zipCode,
                zipCodeError = null,
                errorMessage = null
            )
        }
    }

    fun onAdditionalInfoChange(additionalInfo: String) {
        _state.update {
            it.copy(
                additionalInfo = additionalInfo,
                additionalInfoError = null,
                errorMessage = null
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun validateInput(): Boolean {
        val currentState = _state.value
        val cityResult = addressValidator.validateCity(currentState.city)
        val streetResult = addressValidator.validateStreet(currentState.street)
        val houseResult = addressValidator.validateHouse(currentState.house)
        val apartmentResult = addressValidator.validateApartment(currentState.apartment)
        val zipCodeResult = addressValidator.validateZipCode(currentState.zipCode)

        val cityError = if (cityResult is ValidationResult.Error) validationErrorMapper.mapToMessage(cityResult.error) else null
        val streetError = if (streetResult is ValidationResult.Error) validationErrorMapper.mapToMessage(streetResult.error) else null
        val houseError = if (houseResult is ValidationResult.Error) validationErrorMapper.mapToMessage(houseResult.error) else null
        val apartmentError = if (apartmentResult is ValidationResult.Error) validationErrorMapper.mapToMessage(apartmentResult.error) else null
        val zipCodeError = if (zipCodeResult is ValidationResult.Error) validationErrorMapper.mapToMessage(zipCodeResult.error) else null

        val hasError = cityError != null ||
                streetError != null ||
                houseError != null ||
                apartmentError != null ||
                zipCodeError != null

        if (hasError) {
            _state.update {
                it.copy(
                    cityError = cityError,
                    streetError = streetError,
                    houseError = houseError,
                    apartmentError = apartmentError,
                    zipCodeError = zipCodeError
                )
            }
            return false
        }
        return true
    }
}
