package software.setixx.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: String,
    val password: String
)

@Serializable
data class SignInResponse(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class SignUpRequest(
    val email: String,
    val phone: String,
    val password: String
)

@Serializable
data class SignUpResponse(
    @SerialName("public_id")
    val publicId: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)