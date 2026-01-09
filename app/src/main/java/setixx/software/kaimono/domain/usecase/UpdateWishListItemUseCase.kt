package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.UpdateWishListItem
import setixx.software.kaimono.domain.model.WishList
import setixx.software.kaimono.domain.repository.WishlistRepository
import javax.inject.Inject

class UpdateWishListItemUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(wishListItemId: Long, updateWishListItem: UpdateWishListItem): ApiResult<WishList> =
        wishlistRepository.updateWishListItem(wishListItemId, updateWishListItem)

}