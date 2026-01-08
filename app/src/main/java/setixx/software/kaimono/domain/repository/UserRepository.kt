package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.PasswordUpdate
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.model.UserUpdate

interface UserRepository {
    suspend fun getCurrentUser(): ApiResult<User>

    suspend fun updateUserInfo(userUpdate: UserUpdate): ApiResult<UserUpdate>

    suspend fun updatePassword(updatePassword: PasswordUpdate): ApiResult<String>
}