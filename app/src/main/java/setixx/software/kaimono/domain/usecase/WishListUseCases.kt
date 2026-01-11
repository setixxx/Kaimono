package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.AddWishListItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.UpdateWishListItem
import setixx.software.kaimono.domain.model.WishList
import setixx.software.kaimono.domain.repository.WishlistRepository
import javax.inject.Inject

class GetUserWishListUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
){
    suspend operator fun invoke(): ApiResult<WishList> =
        wishlistRepository.getUserWishList()
}

class AddWishListItemUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
){
    suspend operator fun invoke(addWishListItem: AddWishListItem): ApiResult<WishList> =
        wishlistRepository.addWishListItem(addWishListItem)
}

class ClearWishListUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
){
    suspend operator fun invoke(): ApiResult<WishList> =
        wishlistRepository.clearWishList()
}

class UpdateWishListItemUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(wishListItemId: Long, updateWishListItem: UpdateWishListItem): ApiResult<WishList> =
        wishlistRepository.updateWishListItem(wishListItemId, updateWishListItem)

}

class DeleteWishListItemUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(wishListItemId: Long): ApiResult<WishList> =
        wishlistRepository.deleteWishListItem(wishListItemId)
}