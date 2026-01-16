package software.setixx.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import software.setixx.kaimono.data.remote.dto.UpdatePasswordRequest
import software.setixx.kaimono.data.remote.dto.UpdatePasswordResponse
import software.setixx.kaimono.data.remote.dto.UpdateUserInfoRequest
import software.setixx.kaimono.data.remote.dto.UpdateUserInfoResponse
import software.setixx.kaimono.data.remote.dto.UserInfoResponse

interface UserApi {
    @GET("user/me")
    suspend fun getCurrentUser(): UserInfoResponse

    @POST("user/update-user")
    suspend fun updateUserInfo(@Body updateUserInfoRequest: UpdateUserInfoRequest): UpdateUserInfoResponse

    @POST("user/update-password")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest): UpdatePasswordResponse
}