package setixx.software.kaimono.domain.validation

import java.util.Calendar
import javax.inject.Inject

class PaymentMethodValidator @Inject constructor() {

    fun validateCardNumber(cardNumber: String): ValidationResult {
        if (cardNumber.isBlank()) {
            return ValidationResult.Error(ValidationError.CardNumberEmpty)
        }
        val cleanNumber = cardNumber.replace(" ", "")
        if (!cleanNumber.all { it.isDigit() } || cleanNumber.length != 16) {
            return ValidationResult.Error(ValidationError.CardNumberInvalidFormat)
        }
        return ValidationResult.Success
    }

    fun validateCardHolderName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult.Error(ValidationError.CardHolderNameEmpty)
        }
        return ValidationResult.Success
    }

    fun validateExpiryDate(expiryDate: String): ValidationResult {
        if (expiryDate.isBlank()) {
            return ValidationResult.Error(ValidationError.ExpiryDateEmpty)
        }
        
        if (!expiryDate.matches(Regex("\\d{4}"))) {
             return ValidationResult.Error(ValidationError.ExpiryDateInvalidFormat)
        }

        val month = expiryDate.take(2).toIntOrNull()
        val year = expiryDate.substring(2,4).toIntOrNull()

        if (month == null || year == null || month !in 1..12) {
            return ValidationResult.Error(ValidationError.ExpiryDateInvalidFormat)
        }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return ValidationResult.Error(ValidationError.ExpiryDateExpired)
        }

        return ValidationResult.Success
    }

    fun validateCvv(cvv: String): ValidationResult {
        if (cvv.isBlank()) {
            return ValidationResult.Error(ValidationError.CvvEmpty)
        }
        if (!cvv.all { it.isDigit() } || cvv.length != 3) {
             return ValidationResult.Error(ValidationError.CvvInvalidFormat)
        }
        return ValidationResult.Success
    }
}
