package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.AddCartItem
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.Cart
import software.setixx.kaimono.domain.model.UpdateCartItem

interface CartRepository {
    suspend fun getCart(): ApiResult<Cart>
    suspend fun clearCart(): ApiResult<Cart>
    suspend fun addCartItem(addCartItem: AddCartItem): ApiResult<Cart>
    suspend fun updateCartItem(cartItemId: String, updateCartItem: UpdateCartItem): ApiResult<Cart>
    suspend fun deleteCartItem(cartItemId: String, size: String): ApiResult<Cart>
}