package setixx.software.kaimono.domain.validation

sealed class DomainError {
    data object InvalidCredentials : DomainError()
    data object ServerInternal : DomainError()
    data object UserAlreadyExists : DomainError()
    data object NoInternet : DomainError()
    data object InvalidToken : DomainError()
    data object DataInconsistent : DomainError()
    data class HttpError(val code: Int, val message: String) : DomainError()
    data class Unknown(val message: String?) : DomainError()
}