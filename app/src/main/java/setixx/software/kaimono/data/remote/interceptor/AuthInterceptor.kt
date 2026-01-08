package setixx.software.kaimono.data.remote.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import setixx.software.kaimono.data.local.TokenManager
import setixx.software.kaimono.data.local.TokenRefresher
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefresher: TokenRefresher
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (originalRequest.url.encodedPath.endsWith("auth/logout")) {
            val refreshToken = runBlocking { tokenManager.getRefreshToken() }
            if (!refreshToken.isNullOrBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $refreshToken")
            }
        } else {
            val token = runBlocking { tokenManager.getAccessToken() }
            if (!token.isNullOrBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401 && !originalRequest.url.encodedPath.contains("/auth/")) {
            response.close()

            val newToken = runBlocking { tokenRefresher.refreshToken() }

            if (newToken != null) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return response
    }
}
