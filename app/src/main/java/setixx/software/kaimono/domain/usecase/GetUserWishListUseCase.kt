package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.WishList
import setixx.software.kaimono.domain.repository.WishlistRepository
import javax.inject.Inject

class GetUserWishListUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
){
    suspend operator fun invoke(): ApiResult<WishList> =
        wishlistRepository.getUserWishList()
}