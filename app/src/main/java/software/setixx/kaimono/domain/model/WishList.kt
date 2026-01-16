package software.setixx.kaimono.domain.model

data class WishList(
    val wishListItem: List<WishListItem>
)

data class WishListItem(
    val id: Long,
    val productPublicId: String,
    val productName: String,
    val productDescription: String,
    val productImage: String?,
    val basePrice: String,
    val isAvailable: Boolean,
    val availableSizes: List<WishListItemSize>,
    val addedAt: String
)

data class WishListItemSize(
    val id: Long,
    val size: String,
    val stockQuantity: Int,
    val priceModifier: String,
    val finalPrice: String
)

data class AddWishListItem(
    val productPublicId: String
)