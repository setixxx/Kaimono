package setixx.software.kaimono.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import setixx.software.kaimono.data.remote.AuthApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun baseUrl() = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideAuthApi(baseUrl: String): AuthApi {
        val networkJson = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(AuthApi::class.java)
    }
}