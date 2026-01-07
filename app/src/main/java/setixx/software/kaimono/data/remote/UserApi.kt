package setixx.software.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import setixx.software.kaimono.data.remote.dto.UpdateUserInfoRequest
import setixx.software.kaimono.data.remote.dto.UpdateUserInfoResponse
import setixx.software.kaimono.data.remote.dto.UserInfoResponse

interface UserApi {
    @GET("user/me")
    suspend fun getCurrentUser(): UserInfoResponse

    @POST("user/update-user")
    suspend fun updateUserInfo(@Body updateUserInfoRequest: UpdateUserInfoRequest): UpdateUserInfoResponse
}