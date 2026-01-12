package setixx.software.kaimono.data.repository

import setixx.software.kaimono.data.remote.CartApi
import setixx.software.kaimono.domain.model.AddCartItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Cart
import setixx.software.kaimono.domain.model.DeleteCartItem
import setixx.software.kaimono.domain.model.UpdateCartItem
import setixx.software.kaimono.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi
) : CartRepository {
    override suspend fun getCart(): ApiResult<Cart> {
        TODO("Not yet implemented")
    }

    override suspend fun clearCart(): ApiResult<Cart> {
        TODO("Not yet implemented")
    }

    override suspend fun addCartItem(addCartItem: AddCartItem): ApiResult<Cart> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCartItem(
        cartItemId: Long,
        updateCartItem: UpdateCartItem
    ): ApiResult<Cart> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCartItem(
        cartItemId: Long,
        deleteCartItem: DeleteCartItem
    ): ApiResult<Cart> {
        TODO("Not yet implemented")
    }
}