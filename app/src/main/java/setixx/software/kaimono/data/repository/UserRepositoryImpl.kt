package setixx.software.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import setixx.software.kaimono.data.remote.UserApi
import setixx.software.kaimono.data.remote.dto.UpdatePasswordRequest
import setixx.software.kaimono.data.remote.dto.UpdateUserInfoRequest
import setixx.software.kaimono.data.remote.dto.UpdateUserInfoResponse
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.Gender
import setixx.software.kaimono.domain.model.PasswordUpdate
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.model.UserUpdate
import setixx.software.kaimono.domain.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {

    override suspend fun getCurrentUser(): ApiResult<User> {
        return try {
            val response = userApi.getCurrentUser()
            val user = User(
                id = response.id,
                name = response.name,
                surname = response.surname,
                phone = response.phone,
                email = response.email,
                birthday = response.birthDate,
                gender = Gender.valueOf(response.gender)
            )
            ApiResult.Success(user)
        } catch (e: HttpException) {
            Log.d("UserInfo", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("UserInfo", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun updateUserInfo(userUpdate: UserUpdate): ApiResult<UserUpdate> {
        return try {
            val request = UpdateUserInfoRequest(
                name = userUpdate.name,
                surname = if (userUpdate.surname.isNullOrEmpty()) null else userUpdate.surname,
                phone = userUpdate.phone,
                email = userUpdate.email,
                birthDate = if (userUpdate.birthday.isNullOrEmpty()) null else userUpdate.birthday,
                gender = userUpdate.gender.toString()
            )
            val response = userApi.updateUserInfo(request)

            if (!response.isConsistentWith(request)) {
                ApiResult.Error(DomainError.DataInconsistent)
            } else {
                ApiResult.Success(userUpdate)
            }
        } catch (e: HttpException) {
            Log.d("UserUpdate", e.message())
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("UserUpdate", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun updatePassword(updatePassword: PasswordUpdate): ApiResult<String> {
        return try {
            val request = UpdatePasswordRequest(
                oldPassword = updatePassword.currentPassword,
                newPassword = updatePassword.newPassword
            )
            Log.d("PasswordUpdate", request.toString())
            val response = userApi.updatePassword(request)
            ApiResult.Success(response.passwordUpdateStatus)
        } catch (e: HttpException) {
            Log.d("PasswordUpdate", e.message())
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("PasswordUpdate", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }
}

private fun UpdateUserInfoResponse.isConsistentWith(request: UpdateUserInfoRequest): Boolean {
    return name == request.name &&
            surname == request.surname &&
            phone == request.phone &&
            email == request.email &&
            birthDate == request.birthDate &&
            gender == request.gender
}