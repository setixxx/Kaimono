package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.AuthResult
import setixx.software.kaimono.domain.model.AuthTokens
import setixx.software.kaimono.domain.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthResult<AuthTokens>
    suspend fun signUp(email: String, phone: String, password: String): AuthResult<String>
    suspend fun logout(): AuthResult<Unit>
    suspend fun getCurrentUser(): AuthResult<User>
    suspend fun refreshAccessToken(): AuthResult<String>
    suspend fun isLoggedIn(): Boolean
    suspend fun getSavedEmail(): String?
}