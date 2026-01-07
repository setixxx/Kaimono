package setixx.software.kaimono.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import setixx.software.kaimono.R
import setixx.software.kaimono.data.remote.UserApi
import setixx.software.kaimono.data.remote.dto.UpdateUserInfoRequest
import setixx.software.kaimono.data.remote.dto.UpdateUserInfoResponse
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Gender
import setixx.software.kaimono.domain.model.User
import setixx.software.kaimono.domain.model.UserUpdate
import setixx.software.kaimono.domain.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    @ApplicationContext private val context: Context,
) : UserRepository {
    override suspend fun getCurrentUser(): ApiResult<User> {
        return try {
            val response = userApi.getCurrentUser()
            val user = User(
                id = response.id,
                name = response.name,
                surname = response.surname ?: context.getString(R.string.label_not_specified),
                phone = response.phone,
                email = response.email,
                birthday = response.birthDate ?: context.getString(R.string.label_not_specified),
                gender = Gender.valueOf(response.gender)
            )
            ApiResult.Success(user)
        } catch (e: HttpException) {
            Log.d("UserInfo", e.message())
            val errorMessage = when (e.code()) {
                401 -> context.getString(R.string.error_invalid_token)
                500 -> context.getString(R.string.error_server_internal)
                else -> context.getString(R.string.error_generic_api, e.message())
            }
            ApiResult.Error(errorMessage)
        } catch (e: IOException) {
            ApiResult.Error(context.getString(R.string.error_no_internet))
        } catch (e: Exception) {
            Log.d("UserInfo", e.message.toString())
            ApiResult.Error(context.getString(R.string.error_unknown, e.message))
        }
    }

    override suspend fun updateUserInfo(userUpdate: UserUpdate): ApiResult<UserUpdate> {
        return try {
            val request = UpdateUserInfoRequest(
                name = userUpdate.name,
                surname = userUpdate.surname,
                phone = userUpdate.phone,
                email = userUpdate.email,
                birthDate = userUpdate.birthday,
                gender = userUpdate.gender.toString()
            )
            val response = userApi.updateUserInfo(request)

            if (!response.isConsistentWith(request)) {
                ApiResult.Error(context.getString(R.string.error_data_consistent))
            } else {
                ApiResult.Success(userUpdate)
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                409 -> context.getString(R.string.error_user_exists)
                500 -> context.getString(R.string.error_server_internal)
                else -> context.getString(R.string.error_generic_api, e.message())
            }
            ApiResult.Error(errorMessage)
        } catch (e: IOException) {
            ApiResult.Error(context.getString(R.string.error_no_internet))
        } catch (e: Exception) {
            ApiResult.Error(context.getString(R.string.error_unknown, e.message))
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