package software.setixx.kaimono.domain.model

data class Address (
    val id: Long,
    val city: String,
    val street: String,
    val house: String,
    val apartment: String,
    val code: String,
    val additionalInfo: String,
    val isDefault: Boolean
)

data class CreateAddress(
    val city: String,
    val street: String,
    val house: String,
    val apartment: String,
    val zipCode: String,
    val additionalInfo: String,
    val isDefault: Boolean
)