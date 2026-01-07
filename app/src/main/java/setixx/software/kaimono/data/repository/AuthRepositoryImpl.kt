package setixx.software.kaimono.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import setixx.software.kaimono.R
import setixx.software.kaimono.data.local.TokenManager
import setixx.software.kaimono.data.remote.AuthApi
import setixx.software.kaimono.data.remote.dto.SignInRequest
import setixx.software.kaimono.data.remote.dto.SignUpRequest
import setixx.software.kaimono.domain.model.AuthResult
import setixx.software.kaimono.domain.model.AuthTokens
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context,
) : AuthRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<AuthTokens> {
        return try {
            val response = authApi.signIn(SignInRequest(email, password))
            val tokens = AuthTokens(response.accessToken, response.refreshToken)
            tokenManager.saveTokens(tokens)
            AuthResult.Success(tokens)
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> context.getString(R.string.error_invalid_credentials)
                500 -> context.getString(R.string.error_server_internal)
                else -> context.getString(R.string.error_generic_api, e.message())
            }
            AuthResult.Error(errorMessage)
        } catch (e: IOException) {
            AuthResult.Error(context.getString(R.string.error_no_internet))
        } catch (e: Exception) {
            AuthResult.Error(context.getString(R.string.error_unknown, e.message))
        }
    }

    override suspend fun signUp(
        email: String,
        phone: String,
        password: String
    ): AuthResult<String> {
        return try {
            val response = authApi.signUp(SignUpRequest(email, phone, password))
            AuthResult.Success(response.publicId)
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                409 -> context.getString(R.string.error_user_exists)
                500 -> context.getString(R.string.error_server_internal)
                else -> context.getString(R.string.error_generic_api, e.message())
            }
            AuthResult.Error(errorMessage)
        } catch (e: IOException) {
            AuthResult.Error(context.getString(R.string.error_no_internet))
        } catch (e: Exception) {
            AuthResult.Error(context.getString(R.string.error_unknown, e.message))
        }
    }

    override suspend fun logout(): AuthResult<Unit> {
        tokenManager.clearTokens()
        return AuthResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): AuthResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshAccessToken(): AuthResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.getAccessToken() != null
    }

    override suspend fun getSavedEmail(): String? {
        TODO("Not yet implemented")
    }
}