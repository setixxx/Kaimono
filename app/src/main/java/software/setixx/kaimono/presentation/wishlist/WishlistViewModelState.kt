package software.setixx.kaimono.presentation.wishlist

import software.setixx.kaimono.domain.model.WishList
import software.setixx.kaimono.domain.model.WishListItem

data class WishlistViewModelState(
    val wishlist: WishList? = null,
    val wishlistItem: List<WishListItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
