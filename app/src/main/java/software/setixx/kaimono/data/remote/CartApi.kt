package software.setixx.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import software.setixx.data.dto.AddToCartRequest
import software.setixx.data.dto.CartResponse
import software.setixx.data.dto.UpdateCartItemRequest

interface CartApi {
    @GET("/cart")
    suspend fun getCart(): CartResponse

    @DELETE("/cart")
    suspend fun clearCart(): CartResponse

    @POST("/cart/items")
    suspend fun addToCart(@Body request: AddToCartRequest): CartResponse

    @PATCH("/cart/items/{publicId}")
    suspend fun updateCartItem(@Path("publicId") productPublicId: String, @Body request: UpdateCartItemRequest): CartResponse

    @DELETE("/cart/items/{publicId}")
    suspend fun removeCartItem(
        @Path("publicId") productPublicId: String,
        @Query("size") size: String
    ): CartResponse
}