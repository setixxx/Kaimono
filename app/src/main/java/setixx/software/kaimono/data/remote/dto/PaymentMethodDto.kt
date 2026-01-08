package setixx.software.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodResponse(
    val id: Long,
    @SerialName("card_number_last4")
    val cardNumberLast4: String,

    @SerialName("card_holder_name")
    val cardHolderName: String,

    @SerialName("expiry_month")
    val expiryMonth: Short,

    @SerialName("expiry_year")
    val expiryYear: Short,

    val cvv: String,

    @SerialName("is_default")
    val isDefault: Boolean
)

@Serializable
data class CreatePaymentMethodRequest(
    @SerialName("card_number")
    val cardNumber: String,

    @SerialName("card_holder_name")
    val cardHolderName: String,

    @SerialName("expiry_month")
    val expiryMonth: Short,

    @SerialName("expiry_year")
    val expiryYear: Short,

    val cvv: String,

    @SerialName("is_default")
    val isDefault: Boolean = false
)

@Serializable
data class DeletePaymentMethodResponse(
    val message: String
)