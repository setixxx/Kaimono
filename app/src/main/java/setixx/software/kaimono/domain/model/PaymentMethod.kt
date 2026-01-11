package setixx.software.kaimono.domain.model

data class PaymentMethod(
    val id: Long,
    val paymentType: String,
    val cardNumberLast4: String?,
    val cardHolderName: String?,
    val expiryMonth: Short?,
    val expiryYear: Short?,
    val cvv: String?,
    val isDefault: Boolean
)

data class CreatePaymentMethod(
    val cardNumber: String,
    val cardHolderName: String,
    val expiryMonth: Short,
    val expiryYear: Short,
    val cvv: String,
    val isDefault: Boolean
)
