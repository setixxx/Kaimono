package setixx.software.kaimono.domain.model

import setixx.software.kaimono.domain.validation.DomainError

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: DomainError) : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
}