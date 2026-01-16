package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.AddWishListItem
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.WishList
import software.setixx.kaimono.domain.repository.WishlistRepository
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

class DeleteWishListItemUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(wishListItemPublicId: String): ApiResult<WishList> =
        wishlistRepository.deleteWishListItem(wishListItemPublicId)
}