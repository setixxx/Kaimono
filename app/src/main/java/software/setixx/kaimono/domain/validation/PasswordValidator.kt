package software.setixx.kaimono.domain.validation

class PasswordValidator {

    fun validate(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult.Error(ValidationError.PasswordEmpty)
            password.length < 6 -> ValidationResult.Error(ValidationError.PasswordTooShort)
            !hasRequiredComplexity(password) -> ValidationResult.Error(ValidationError.PasswordLowComplexity)
            else -> ValidationResult.Success
        }
    }

    fun validateConfirmation(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isEmpty() -> ValidationResult.Error(ValidationError.ConfirmPasswordEmpty)
            password != confirmPassword -> ValidationResult.Error(ValidationError.PasswordsDoNotMatch)
            else -> ValidationResult.Success
        }
    }

    private fun hasRequiredComplexity(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        return hasUpperCase && hasLowerCase && hasDigit
    }
}