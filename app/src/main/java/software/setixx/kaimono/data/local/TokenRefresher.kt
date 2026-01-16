package software.setixx.kaimono.data.local

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import software.setixx.kaimono.data.remote.AuthApi
import software.setixx.kaimono.data.remote.dto.RefreshTokenRequest
import software.setixx.kaimono.domain.model.AuthTokens
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefresher @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    private val mutex = Mutex()

    suspend fun refreshToken(): String? {
        return mutex.withLock {
            try {
                val refreshToken = tokenManager.getRefreshToken() ?: return null
                val response = authApi.refreshToken(RefreshTokenRequest(refreshToken))
                val tokens = AuthTokens(response.accessToken, response.refreshToken)
                tokenManager.saveTokens(tokens)
                response.accessToken
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    tokenManager.clearTokens()
                }
                null
            } catch (e: Exception) {
                null
            }
        }
    }
}