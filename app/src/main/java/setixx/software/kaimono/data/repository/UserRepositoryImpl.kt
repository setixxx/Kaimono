package setixx.software.kaimono.data.repository

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.toUpperCase
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import setixx.software.kaimono.R
import setixx.software.kaimono.data.local.TokenManager
import setixx.software.kaimono.data.remote.UserApi
import setixx.software.kaimono.data.remote.interceptor.AuthInterceptor
import setixx.software.kaimono.domain.model.AuthResult
import setixx.software.kaimono.domain.model.Gender
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    @ApplicationContext private val context: Context,
) : UserRepository {
    override suspend fun getCurrentUser(): AuthResult<User> {
        return try {
            val response = userApi.getCurrentUser()
            val user = User(
                id = response.id,
                name = response.name,
                surname = response.surname,
                phone = response.phone,
                email = response.email,
                birthday = response.birthDate,
                gender = Gender.valueOf(response.gender.uppercase())
            )
            AuthResult.Success(user)
        } catch (e: HttpException) {
            Log.d("UserInfo", e.message())
            val errorMessage = when (e.code()) {
                401 -> context.getString(R.string.error_invalid_token)
                500 -> context.getString(R.string.error_server_internal)
                else -> context.getString(R.string.error_generic_api, e.message())
            }
            AuthResult.Error(errorMessage)
        } catch (e: IOException) {
            AuthResult.Error(context.getString(R.string.error_no_internet))
        } catch (e: Exception) {
            Log.d("UserInfo", e.message.toString())
            AuthResult.Error(context.getString(R.string.error_unknown, e.message))
        }
    }
}