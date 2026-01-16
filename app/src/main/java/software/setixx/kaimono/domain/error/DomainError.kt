package software.setixx.kaimono.domain.error

sealed class DomainError {
    data object InvalidCredentials : DomainError()
    data object ServerInternal : DomainError()
    data object UserAlreadyExists : DomainError()
    data object NoInternet : DomainError()
    data object InvalidToken : DomainError()
    data object DataInconsistent : DomainError()
    data object InvalidData : DomainError()
    data object NotFound : DomainError()
    data class HttpError(val code: Int, val message: String) : DomainError()
    data class Unknown(val message: String?) : DomainError()
}