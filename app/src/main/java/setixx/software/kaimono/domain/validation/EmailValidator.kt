package setixx.software.kaimono.domain.validation

class EmailValidator {
    fun validate(email: String): ValidationResult {
        val trimmedEmail = email.trim()

        return when {
            trimmedEmail.isEmpty() -> ValidationResult.Error(ValidationError.EmailEmpty)
            !isValidFormat(trimmedEmail) -> ValidationResult.Error(ValidationError.EmailInvalidFormat)
            else -> ValidationResult.Success
        }
    }

    private fun isValidFormat(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }
}