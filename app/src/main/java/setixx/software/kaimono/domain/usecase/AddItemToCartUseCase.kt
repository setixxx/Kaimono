package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.AddCartItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Cart
import setixx.software.kaimono.domain.repository.CartRepository
import javax.inject.Inject

class AddItemToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(addCartItem: AddCartItem): ApiResult<Cart> =
        cartRepository.addCartItem(addCartItem)
}