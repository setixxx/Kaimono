package setixx.software.kaimono.data.repository

import retrofit2.HttpException
import setixx.software.kaimono.data.local.TokenManager
import setixx.software.kaimono.data.remote.AuthApi
import setixx.software.kaimono.data.remote.dto.SignInRequest
import setixx.software.kaimono.data.remote.dto.SignUpRequest
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.AuthTokens
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    @Named("public") private val publicAuthApi: AuthApi,
    @Named("refresh") private val refreshAuthApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): ApiResult<AuthTokens> {
        return try {
            val response = publicAuthApi.signIn(SignInRequest(email, password))
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
            val response = publicAuthApi.signUp(SignUpRequest(email, phone, password))
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
        return try {
            refreshAuthApi.logout()
            tokenManager.clearTokens()
            ApiResult.Success(Unit)
        } catch (e: HttpException) {
            tokenManager.clearTokens()
            val error = when (e.code()) {
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            tokenManager.clearTokens()
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            tokenManager.clearTokens()
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun refreshAccessToken(): ApiResult<String> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
                ?: return ApiResult.Error(DomainError.InvalidToken)

            val response = refreshAuthApi.refreshToken(
                setixx.software.kaimono.data.remote.dto.RefreshTokenRequest(refreshToken)
            )
            val tokens = AuthTokens(response.accessToken, response.refreshToken)
            tokenManager.saveTokens(tokens)
            ApiResult.Success(response.accessToken)
        } catch (e: HttpException) {
            tokenManager.clearTokens()
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
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

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.getAccessToken() != null
    }
}