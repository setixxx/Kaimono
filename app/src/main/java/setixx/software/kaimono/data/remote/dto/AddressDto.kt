package setixx.software.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressResponse(
    val id: Long,
    val city: String,
    val street: String,
    val house: String,
    val apartment: String,
    @SerialName("zip_code")
    val code: String,
    @SerialName("additional_info")
    val additionalInfo: String,
    @SerialName("is_default")
    val isDefault: Boolean
)

@Serializable
data class DeleteAddressResponse(
    val message: String
)

@Serializable
data class CreateAddressRequest(
    val city: String,
    val street: String,
    val house: String,
    val apartment: String,
    @SerialName("zip_code")
    val code: String,
    @SerialName("additional_info")
    val additionalInfo: String,
    @SerialName("is_default")
    val isDefault: Boolean
)