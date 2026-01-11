package setixx.software.kaimono.domain.model

data class ProductList(
    val products: List<Product>,
    val totalCount: Long,
    val page: Int,
    val pageSize: Int
)

data class Product(
    val id: Long,
    val publicId: String,
    val name: String,
    val description: String,
    val basePrice: String,
    val isAvailable: Boolean,
    val categories: List<Category>,
    val sizes: List<Size>,
    val images: List<Image>,
    val averageRating: Double?,
    val reviewCount: Long
)

data class Size(
    val id: Long,
    val size: String,
    val stockQuantity: Int,
    val priceModifier: String
)

data class Image(
    val id: Long,
    val imageUrl: String,
    val isPrimary: Boolean,
    val displayOrder: Int
)

data class ProductRequest(
    val query: String? = null,
    val categoryIds: String? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val inStockOnly: Boolean? = null,
    val sortBy: String? = null,
    val sortOrder: String? = null,
    val page: Int? = null,
    val pageSize: Int? = null
)