package software.setixx.kaimono.domain.model

data class Order(
    val publicId: String,
    val status: String,
    val totalAmount: String,
    val createdAt: String,
    val items: List<OrderItem>,
    val deliveryInfo: DeliveryInfo?,
    val paymentInfo: PaymentInfo?
)

data class OrderItem(
    val productPublicId: String,
    val productName: String,
    val size: String,
    val quantity: Int,
    val priceAtPurchase: String,
    val subtotal: String
)

data class DeliveryInfo(
    val trackingNumber: String?,
    val status: String,
    val estimatedDate: String?,
    val address: Address
)

data class PaymentInfo(
    val id: Long,
    val amount: String,
    val status: String,
    val paymentType: String,
    val transactionId: String?,
    val paidAt: String?,
    val paymentMethod: PaymentMethodInfo?
)

data class PaymentMethodInfo(
    val cardNumberLast4: String,
    val cardHolderName: String
)

data class CreateOrder(
    val addressId: Long,
    val paymentMethodId: Long?,
    val paymentMethod: String
)