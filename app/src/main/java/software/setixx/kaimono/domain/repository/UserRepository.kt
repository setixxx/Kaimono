package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.PasswordUpdate
import software.setixx.kaimono.domain.model.User
import software.setixx.kaimono.domain.model.UserUpdate

interface UserRepository {
    suspend fun getCurrentUser(): ApiResult<User>

    suspend fun updateUserInfo(userUpdate: UserUpdate): ApiResult<UserUpdate>

    suspend fun updatePassword(updatePassword: PasswordUpdate): ApiResult<String>
}