package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.AddCartItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Cart
import setixx.software.kaimono.domain.model.DeleteCartItem
import setixx.software.kaimono.domain.model.UpdateCartItem

interface CartRepository {
    suspend fun getCart(): ApiResult<Cart>
    suspend fun clearCart(): ApiResult<Cart>
    suspend fun addCartItem(addCartItem: AddCartItem): ApiResult<Cart>
    suspend fun updateCartItem(cartItemId: Long, updateCartItem: UpdateCartItem): ApiResult<Cart>
    suspend fun deleteCartItem(cartItemId: Long, deleteCartItem: DeleteCartItem): ApiResult<Cart>
}