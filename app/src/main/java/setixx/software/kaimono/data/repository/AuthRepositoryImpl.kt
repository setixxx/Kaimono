package setixx.software.kaimono.data.repository

import retrofit2.HttpException
import setixx.software.kaimono.data.local.TokenManager
import setixx.software.kaimono.data.remote.AuthApi
import setixx.software.kaimono.data.remote.dto.SignInRequest
import setixx.software.kaimono.data.remote.dto.SignUpRequest
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.AuthTokens
import setixx.software.kaimono.domain.validation.DomainError
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): ApiResult<AuthTokens> {
        return try {
            val response = authApi.signIn(SignInRequest(email, password))
            val tokens = AuthTokens(response.accessToken, response.refreshToken)
            tokenManager.saveTokens(tokens)
            ApiResult.Success(tokens)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                401 -> DomainError.InvalidCredentials
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun signUp(
        email: String,
        phone: String,
        password: String
    ): ApiResult<String> {
        return try {
            val response = authApi.signUp(SignUpRequest(email, phone, password))
            ApiResult.Success(response.publicId)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                409 -> DomainError.UserAlreadyExists
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun logout(): ApiResult<Unit> {
        tokenManager.clearTokens()
        return ApiResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): ApiResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshAccessToken(): ApiResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.getAccessToken() != null
    }

    override suspend fun getSavedEmail(): String? {
        TODO("Not yet implemented")
    }
}