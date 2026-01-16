package software.setixx.kaimono.domain.validation

import javax.inject.Inject

class AddressValidator @Inject constructor() {

    fun validateCity(city: String): ValidationResult {
        if (city.isBlank()) {
            return ValidationResult.Error(ValidationError.CityEmpty)
        }
        return ValidationResult.Success
    }

    fun validateStreet(street: String): ValidationResult {
        if (street.isBlank()) {
            return ValidationResult.Error(ValidationError.StreetEmpty)
        }
        return ValidationResult.Success
    }

    fun validateHouse(house: String): ValidationResult {
        if (house.isBlank()) {
            return ValidationResult.Error(ValidationError.HouseEmpty)
        }
        return ValidationResult.Success
    }

    fun validateApartment(apartment: String): ValidationResult {
        if (apartment.isBlank()) {
            return ValidationResult.Error(ValidationError.ApartmentEmpty)
        }
        return ValidationResult.Success
    }

    fun validateZipCode(zipCode: String): ValidationResult {
        if (zipCode.isBlank()) {
            return ValidationResult.Error(ValidationError.ZipCodeEmpty)
        }
        if (!zipCode.all { it.isDigit() }) {
            return ValidationResult.Error(ValidationError.ZipCodeInvalidFormat)
        }
        return ValidationResult.Success
    }
}