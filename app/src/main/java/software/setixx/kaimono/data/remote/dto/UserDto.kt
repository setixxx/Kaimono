package software.setixx.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val id: String,
    val name: String,
    val surname: String?,
    val phone: String,
    val email: String,
    @SerialName("birth_date")
    val birthDate: String?,
    val gender: String
)

@Serializable
data class UpdateUserInfoRequest(
    val name: String,
    val surname: String?,
    val phone: String,
    val email: String,
    @SerialName("birth_date")
    val birthDate: String?,
    val gender: String
)

@Serializable
data class UpdateUserInfoResponse(
    val id: String,
    val name: String,
    val surname: String?,
    val phone: String,
    val email: String,
    @SerialName("birth_date")
    val birthDate: String?,
    val gender: String
)

@Serializable
data class UpdatePasswordRequest(
    @SerialName("old_password")
    val oldPassword: String,
    @SerialName("new_password")
    val newPassword: String,
)

@Serializable
data class UpdatePasswordResponse(
    @SerialName("password_update_status")
    val passwordUpdateStatus: String
)