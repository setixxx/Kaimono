package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.AddWishListItem
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.WishList

interface WishlistRepository {
    suspend fun getUserWishList(): ApiResult<WishList>
    suspend fun addWishListItem(addWishListItem: AddWishListItem): ApiResult<WishList>
    suspend fun clearWishList(): ApiResult<WishList>
    suspend fun deleteWishListItem(wishListItemPublicId: String): ApiResult<WishList>
}