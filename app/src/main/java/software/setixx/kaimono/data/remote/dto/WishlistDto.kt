package software.setixx.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToWishlistRequest(
    @SerialName("product_public_id")
    val productPublicId: String
)

@Serializable
data class WishlistResponse(
    val items: List<WishlistItemResponse>
)

@Serializable
data class WishlistItemResponse(
    val id: Long,

    @SerialName("product_public_id")
    val productPublicId: String,

    @SerialName("product_name")
    val productName: String,

    @SerialName("product_description")
    val productDescription: String,

    @SerialName("product_image")
    val productImage: String?,

    @SerialName("base_price")
    val basePrice: String,

    @SerialName("is_available")
    val isAvailable: Boolean,

    @SerialName("available_sizes")
    val availableSizes: List<ProductSizeInfo>,

    @SerialName("added_at")
    val addedAt: String
)

@Serializable
data class ProductSizeInfo(
    val id: Long,
    val size: String,

    @SerialName("stock_quantity")
    val stockQuantity: Int,

    @SerialName("price_modifier")
    val priceModifier: String,

    @SerialName("final_price")
    val finalPrice: String
)