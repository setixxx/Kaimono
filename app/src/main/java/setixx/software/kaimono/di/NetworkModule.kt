package setixx.software.kaimono.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import setixx.software.kaimono.data.local.TokenManager
import setixx.software.kaimono.data.local.TokenRefresher
import setixx.software.kaimono.data.remote.AuthApi
import setixx.software.kaimono.data.remote.UserApi
import setixx.software.kaimono.data.remote.interceptor.AuthInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun baseUrl() = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideAuthApi(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): AuthApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenRefresher(
        authApi: AuthApi,
        tokenManager: TokenManager
    ): TokenRefresher {
        return TokenRefresher(authApi, tokenManager)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
        tokenRefresher: TokenRefresher
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager, tokenRefresher)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): UserApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(UserApi::class.java)
    }
}