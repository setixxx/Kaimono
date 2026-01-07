package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.AuthTokens
import setixx.software.kaimono.domain.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): ApiResult<AuthTokens>
    suspend fun signUp(email: String, phone: String, password: String): ApiResult<String>
    suspend fun logout(): ApiResult<Unit>
    suspend fun getCurrentUser(): ApiResult<User>
    suspend fun refreshAccessToken(): ApiResult<String>
    suspend fun isLoggedIn(): Boolean
    suspend fun getSavedEmail(): String?
}