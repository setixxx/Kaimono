package setixx.software.kaimono.domain.validation

class NameValidator {

    fun validateName(name: String): ValidationResult {
        val trimmedName = name.trim()

        return when {
            trimmedName.isEmpty() -> ValidationResult.Error(ValidationError.NameEmpty)
            !isValidFormat(trimmedName) -> ValidationResult.Error(ValidationError.NameInvalidFormat)
            else -> ValidationResult.Success
        }
    }

    fun validateSurname(surname: String): ValidationResult {
        val trimmedSurname = surname.trim()

        return when {
            trimmedSurname.isEmpty() -> ValidationResult.Error(ValidationError.SurnameEmpty)
            !isValidFormat(trimmedSurname) -> ValidationResult.Error(ValidationError.SurnameInvalidFormat)
            else -> ValidationResult.Success
        }
    }

    private fun isValidFormat(text: String): Boolean {
        return text.matches(Regex("^[a-zA-Zа-яА-Я]+$"))
    }
}