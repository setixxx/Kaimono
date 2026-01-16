package software.setixx.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    val products: List<ProductResponse>,

    @SerialName("total_count")
    val totalCount: Long,

    val page: Int,

    @SerialName("page_size")
    val pageSize: Int
)

@Serializable
data class ProductResponse(
    val id: Long,

    @SerialName("public_id")
    val publicId: String,

    val name: String,
    val description: String,

    @SerialName("base_price")
    val basePrice: String,

    @SerialName("is_available")
    val isAvailable: Boolean,

    val categories: List<CategoryResponse>,
    val sizes: List<ProductSizeResponse>,
    val images: List<ProductImageResponse>,
    @SerialName("average_rating")
    val averageRating: Double?,

    @SerialName("review_count")
    val reviewCount: Long
)

@Serializable
data class ProductSizeResponse(
    val id: Long,
    val size: String,

    @SerialName("stock_quantity")
    val stockQuantity: Int,

    @SerialName("price_modifier")
    val priceModifier: String
)

@Serializable
data class ProductImageResponse(
    val id: Long,

    @SerialName("image_url")
    val imageUrl: String,

    @SerialName("is_primary")
    val isPrimary: Boolean,

    @SerialName("display_order")
    val displayOrder: Int
)