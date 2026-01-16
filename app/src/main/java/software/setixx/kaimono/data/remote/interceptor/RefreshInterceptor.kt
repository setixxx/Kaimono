package software.setixx.kaimono.data.remote.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import software.setixx.kaimono.data.local.TokenManager
import javax.inject.Inject

class RefreshInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val refreshToken = runBlocking { tokenManager.getRefreshToken() }

        if (!refreshToken.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $refreshToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}