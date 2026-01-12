package setixx.software.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import setixx.software.data.dto.AddToCartRequest
import setixx.software.data.dto.UpdateCartItemRequest
import setixx.software.kaimono.data.remote.CartApi
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.AddCartItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Cart
import setixx.software.kaimono.domain.model.CartItem
import setixx.software.kaimono.domain.model.UpdateCartItem
import setixx.software.kaimono.domain.repository.CartRepository
import java.io.IOException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi
) : CartRepository {
    override suspend fun getCart(): ApiResult<Cart> {
        return try {
            val response = cartApi.getCart()
            val cart = Cart(
                id = response.id,
                items = response.items.map { item ->
                    CartItem(
                        id = item.id,
                        productPublicId = item.productPublicId,
                        productName = item.productName,
                        productImage = item.productImage,
                        size = item.size,
                        quantity = item.quantity,
                        pricePerItem = item.pricePerItem,
                        subtotal = item.subtotal
                    )
                },
                totalPrice = response.totalPrice
            )
            ApiResult.Success(cart)
        } catch (e: HttpException) {
            Log.d("Address", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Address", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun clearCart(): ApiResult<Cart> {
        return try {
            val response = cartApi.clearCart()
            val cart = Cart(
                id = response.id,
                items = response.items.map { item ->
                    CartItem(
                        id = item.id,
                        productPublicId = item.productPublicId,
                        productName = item.productName,
                        productImage = item.productImage,
                        size = item.size,
                        quantity = item.quantity,
                        pricePerItem = item.pricePerItem,
                        subtotal = item.subtotal
                    )
                },
                totalPrice = response.totalPrice
            )
            ApiResult.Success(cart)
        } catch (e: HttpException) {
            Log.d("Cart", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Cart", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun addCartItem(addCartItem: AddCartItem): ApiResult<Cart> {
        return try {
            val request = AddToCartRequest(
                productPublicId = addCartItem.productId,
                size = addCartItem.size,
                quantity = addCartItem.quantity
            )
            val response = cartApi.addToCart(request)
            val cart = Cart(
                id = response.id,
                items = response.items.map { item ->
                    CartItem(
                        id = item.id,
                        productPublicId = item.productPublicId,
                        productName = item.productName,
                        productImage = item.productImage,
                        size = item.size,
                        quantity = item.quantity,
                        pricePerItem = item.pricePerItem,
                        subtotal = item.subtotal
                    )
                },
                totalPrice = response.totalPrice
            )
            ApiResult.Success(cart)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun updateCartItem(
        cartItemId: String,
        updateCartItem: UpdateCartItem
    ): ApiResult<Cart> {
        return try {
            val request = UpdateCartItemRequest(
                size = updateCartItem.size,
                quantity = updateCartItem.quantity
            )
            val response = cartApi.updateCartItem(cartItemId, request)
            val cart = Cart(
                id = response.id,
                items = response.items.map { item ->
                    CartItem(
                        id = item.id,
                        productPublicId = item.productPublicId,
                        productName = item.productName,
                        productImage = item.productImage,
                        size = item.size,
                        quantity = item.quantity,
                        pricePerItem = item.pricePerItem,
                        subtotal = item.subtotal
                    )
                },
                totalPrice = response.totalPrice
            )
            ApiResult.Success(cart)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun deleteCartItem(
        cartItemId: String,
        size: String
    ): ApiResult<Cart> {
        return try {
            val response = cartApi.removeCartItem(cartItemId, size)
            val cart = Cart(
                id = response.id,
                items = response.items.map { item ->
                    CartItem(
                        id = item.id,
                        productPublicId = item.productPublicId,
                        productName = item.productName,
                        productImage = item.productImage,
                        size = item.size,
                        quantity = item.quantity,
                        pricePerItem = item.pricePerItem,
                        subtotal = item.subtotal
                    )
                },
                totalPrice = response.totalPrice
            )
            ApiResult.Success(cart)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }
}