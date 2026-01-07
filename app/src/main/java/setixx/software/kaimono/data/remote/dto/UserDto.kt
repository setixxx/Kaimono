package setixx.software.kaimono.data.remote.dto

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