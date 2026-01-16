package software.setixx.kaimono.domain.model

import software.setixx.kaimono.domain.error.DomainError

sealed class ApiResult<out T> {
    data class Success<T>(val data: T, val message: T? = null) : ApiResult<T>()
    data class Error(val error: DomainError) : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
}