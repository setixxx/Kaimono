package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.AddWishListItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.UpdateWishListItem
import setixx.software.kaimono.domain.model.WishList

interface WishlistRepository {
    suspend fun getUserWishList(): ApiResult<WishList>
    suspend fun addWishListItem(addWishListItem: AddWishListItem): ApiResult<WishList>
    suspend fun clearWishList(): ApiResult<WishList>
    suspend fun updateWishListItem(wishListItemId: Long, updateWishListItem: UpdateWishListItem): ApiResult<WishList>
    suspend fun deleteWishListItem(wishListItemId: Long): ApiResult<WishList>
}