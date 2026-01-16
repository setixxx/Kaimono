package software.setixx.kaimono.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import software.setixx.kaimono.data.local.TokenManager
import software.setixx.kaimono.data.local.TokenRefresher
import software.setixx.kaimono.data.remote.AddressApi
import software.setixx.kaimono.data.remote.AuthApi
import software.setixx.kaimono.data.remote.CartApi
import software.setixx.kaimono.data.remote.CategoryApi
import software.setixx.kaimono.data.remote.OrderApi
import software.setixx.kaimono.data.remote.PaymentMethodApi
import software.setixx.kaimono.data.remote.ProductApi
import software.setixx.kaimono.data.remote.ReviewApi
import software.setixx.kaimono.data.remote.UserApi
import software.setixx.kaimono.data.remote.WishlistApi
import software.setixx.kaimono.data.remote.interceptor.AuthInterceptor
import software.setixx.kaimono.data.remote.interceptor.RefreshInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun baseUrl() = "http://192.168.1.92:8080/"

    @Provides
    @Singleton
    @Named("public")
    fun providePublicOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRefreshInterceptor(tokenManager: TokenManager): RefreshInterceptor {
        return RefreshInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    @Named("refresh")
    fun provideRefreshOkHttpClient(refreshInterceptor: RefreshInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(refreshInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("refresh")
    fun provideRefreshAuthApi(
        baseUrl: String,
        @Named("refresh") okHttpClient: OkHttpClient
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
        @Named("refresh") authApi: AuthApi,
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
    @Named("protected")
    fun provideProtectedOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("public")
    fun providePublicAuthApi(
        baseUrl: String,
        @Named("public") okHttpClient: OkHttpClient
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
    @Named("protected")
    fun provideProtectedAuthApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
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
    fun provideUserApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
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

    @Provides
    @Singleton
    fun provideAddressApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): AddressApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(AddressApi::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentMethodApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): PaymentMethodApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(PaymentMethodApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): ProductApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): ReviewApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(ReviewApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): CategoryApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWishlistApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): WishlistApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(WishlistApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCartApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): CartApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(CartApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderApi(
        baseUrl: String,
        @Named("protected") okHttpClient: OkHttpClient
    ): OrderApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(OrderApi::class.java)
    }
}