package software.setixx.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import software.setixx.kaimono.data.remote.UserApi
import software.setixx.kaimono.data.remote.dto.UpdatePasswordRequest
import software.setixx.kaimono.data.remote.dto.UpdateUserInfoRequest
import software.setixx.kaimono.data.remote.dto.UpdateUserInfoResponse
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.error.DomainError
import software.setixx.kaimono.domain.model.Gender
import software.setixx.kaimono.domain.model.PasswordUpdate
import software.setixx.kaimono.domain.model.User
import software.setixx.kaimono.domain.model.UserUpdate
import software.setixx.kaimono.domain.repository.UserRepository
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
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
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
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun updatePassword(updatePassword: PasswordUpdate): ApiResult<String> {
        return try {
            val request = UpdatePasswordRequest(
                oldPassword = updatePassword.currentPassword,
                newPassword = updatePassword.newPassword
            )
            val response = userApi.updatePassword(request)
            ApiResult.Success(response.passwordUpdateStatus)
        } catch (e: HttpException) {
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