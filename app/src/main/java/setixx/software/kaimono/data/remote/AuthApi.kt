package setixx.software.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import setixx.software.kaimono.data.remote.dto.RefreshTokenRequest
import setixx.software.kaimono.data.remote.dto.RefreshTokenResponse
import setixx.software.kaimono.data.remote.dto.SignInRequest
import setixx.software.kaimono.data.remote.dto.SignInResponse
import setixx.software.kaimono.data.remote.dto.SignUpRequest
import setixx.software.kaimono.data.remote.dto.SignUpResponse

interface AuthApi {
    @POST("auth/login")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @POST("auth/register")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    @POST("auth/logout")
    suspend fun logout()
}