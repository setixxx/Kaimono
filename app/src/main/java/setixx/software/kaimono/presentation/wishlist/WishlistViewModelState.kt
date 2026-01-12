package setixx.software.kaimono.presentation.wishlist

import setixx.software.kaimono.domain.model.Category
import setixx.software.kaimono.domain.model.Product
import setixx.software.kaimono.domain.model.WishList
import setixx.software.kaimono.domain.model.WishListItem

data class WishlistViewModelState(
    val wishlist: WishList? = null,
    val wishlistItem: List<WishListItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
