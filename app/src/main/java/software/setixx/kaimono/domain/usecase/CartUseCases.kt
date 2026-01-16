package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.AddCartItem
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.Cart
import software.setixx.kaimono.domain.model.UpdateCartItem
import software.setixx.kaimono.domain.repository.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): ApiResult<Cart> =
        cartRepository.getCart()
}

class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): ApiResult<Cart> =
        cartRepository.clearCart()
}

class AddCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(addCartItem: AddCartItem): ApiResult<Cart> =
        cartRepository.addCartItem(addCartItem)
}

class UpdateCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: String, updateCartItem: UpdateCartItem): ApiResult<Cart> =
        cartRepository.updateCartItem(cartItemId, updateCartItem)
}

class DeleteCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: String, size: String): ApiResult<Cart> =
        cartRepository.deleteCartItem(cartItemId, size)
}
