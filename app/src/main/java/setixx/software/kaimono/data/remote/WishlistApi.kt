package setixx.software.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import setixx.software.kaimono.data.remote.dto.AddToWishlistRequest
import setixx.software.kaimono.data.remote.dto.WishlistResponse
import setixx.software.kaimono.domain.model.AddWishListItem

interface WishlistApi {
    @GET("/wishlist")
    suspend fun getUserWishlist(): WishlistResponse

    @POST("/wishlist")
    suspend fun addWishlistItem(@Body addToWishlistRequest: AddToWishlistRequest): WishlistResponse

    @DELETE("/wishlist")
    suspend fun clearWishlist(): WishlistResponse

    @DELETE("/wishlist/{id}")
    suspend fun deleteWishlistItem(@Path("id") wishListItemId: String): WishlistResponse
}