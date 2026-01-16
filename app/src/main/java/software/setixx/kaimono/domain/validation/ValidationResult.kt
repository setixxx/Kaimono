package software.setixx.kaimono.domain.validation

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val error: ValidationError) : ValidationResult()

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error

    fun getErrorOrNull(): ValidationError? = when (this) {
        is Error -> error
        is Success -> null
    }
}