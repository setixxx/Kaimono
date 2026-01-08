package setixx.software.kaimono.domain.validation

class PhoneValidator {

    fun validate(phone: String): ValidationResult {
        val trimmedPhone = phone.trim()

        return when {
            trimmedPhone.isEmpty() -> ValidationResult.Error(ValidationError.PhoneEmpty)
            !isValidFormat(trimmedPhone) -> ValidationResult.Error(ValidationError.PhoneInvalidFormat)
            else -> ValidationResult.Success
        }
    }

    private fun isValidFormat(phone: String): Boolean {
        return phone.matches(Regex("^\\d{11}$"))
    }
}