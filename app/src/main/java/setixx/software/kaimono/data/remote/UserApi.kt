package setixx.software.kaimono.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import setixx.software.kaimono.data.remote.dto.UserInfoResponse

interface UserApi {
    @GET("user/me")
    suspend fun getCurrentUser(): UserInfoResponse
}