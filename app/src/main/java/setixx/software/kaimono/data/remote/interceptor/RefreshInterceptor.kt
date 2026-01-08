package setixx.software.kaimono.data.remote.interceptor

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import setixx.software.kaimono.data.local.TokenManager
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