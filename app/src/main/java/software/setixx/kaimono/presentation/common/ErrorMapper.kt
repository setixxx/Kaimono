package software.setixx.kaimono.presentation.common

import android.content.Context
import software.setixx.kaimono.R
import software.setixx.kaimono.domain.error.DomainError

class ErrorMapper(private val context: Context) {

    fun mapToMessage(error: DomainError): String {
        return when (error) {
            is DomainError.InvalidCredentials ->
                context.getString(R.string.error_invalid_credentials)

            is DomainError.ServerInternal ->
                context.getString(R.string.error_server_internal)

            is DomainError.UserAlreadyExists ->
                context.getString(R.string.error_user_exists)

            is DomainError.NoInternet ->
                context.getString(R.string.error_no_internet)

            is DomainError.InvalidToken ->
                context.getString(R.string.error_invalid_token)

            is DomainError.DataInconsistent ->
                context.getString(R.string.error_data_consistent)

            is DomainError.InvalidData ->
                context.getString(R.string.error_invalid_data)

            is DomainError.HttpError ->
                context.getString(R.string.error_generic_api, error.message)

            is DomainError.NotFound ->
                context.getString(R.string.error_not_found)

            is DomainError.Unknown ->
                context.getString(R.string.error_unknown, error.message ?: "")
        }
    }
}