package software.setixx.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import software.setixx.kaimono.data.remote.WishlistApi
import software.setixx.kaimono.data.remote.dto.AddToWishlistRequest
import software.setixx.kaimono.domain.error.DomainError
import software.setixx.kaimono.domain.model.AddWishListItem
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.WishList
import software.setixx.kaimono.domain.model.WishListItem
import software.setixx.kaimono.domain.model.WishListItemSize
import software.setixx.kaimono.domain.repository.WishlistRepository
import java.io.IOException
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistApi: WishlistApi
) : WishlistRepository {
    override suspend fun getUserWishList(): ApiResult<WishList> {
        return try {
            val response = wishlistApi.getUserWishlist()
            val wishList = WishList(
                wishListItem = response.items.map {
                    WishListItem(
                        id = it.id,
                        productPublicId = it.productPublicId,
                        productName = it.productName,
                        productDescription = it.productDescription,
                        productImage = it.productImage,
                        basePrice = it.basePrice,
                        isAvailable = it.isAvailable,
                        availableSizes = it.availableSizes.map { size ->
                            WishListItemSize(
                                id = size.id,
                                size = size.size,
                                stockQuantity = size.stockQuantity,
                                priceModifier = size.priceModifier,
                                finalPrice = size.finalPrice
                            )
                        },
                        addedAt = it.addedAt
                    )
                }
            )
            ApiResult.Success(wishList)
        } catch (e: HttpException) {
            Log.d("Wishlist", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Wishlist", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun addWishListItem(addWishListItem: AddWishListItem): ApiResult<WishList> {
        return try {
            val request = AddToWishlistRequest(addWishListItem.productPublicId)
            val response = wishlistApi.addWishlistItem(request)
            val wishList = WishList(
                wishListItem = response.items.map {
                    WishListItem(
                        id = it.id,
                        productPublicId = it.productPublicId,
                        productName = it.productName,
                        productDescription = it.productDescription,
                        productImage = it.productImage,
                        basePrice = it.basePrice,
                        isAvailable = it.isAvailable,
                        availableSizes = it.availableSizes.map { size ->
                            WishListItemSize(
                                id = size.id,
                                size = size.size,
                                stockQuantity = size.stockQuantity,
                                priceModifier = size.priceModifier,
                                finalPrice = size.finalPrice
                            )
                        },
                        addedAt = it.addedAt
                    )
                }
            )
            ApiResult.Success(wishList)
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

    override suspend fun clearWishList(): ApiResult<WishList> {
        return try {
            val response = wishlistApi.clearWishlist()
            val wishList = WishList(
                wishListItem = response.items.map {
                    WishListItem(
                        id = it.id,
                        productPublicId = it.productPublicId,
                        productName = it.productName,
                        productDescription = it.productDescription,
                        productImage = it.productImage,
                        basePrice = it.basePrice,
                        isAvailable = it.isAvailable,
                        availableSizes = it.availableSizes.map { size ->
                            WishListItemSize(
                                id = size.id,
                                size = size.size,
                                stockQuantity = size.stockQuantity,
                                priceModifier = size.priceModifier,
                                finalPrice = size.finalPrice
                            )
                        },
                        addedAt = it.addedAt
                    )
                }
            )
            ApiResult.Success(wishList)
        } catch (e: HttpException) {
            val error = when (e.code()) {
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

    override suspend fun deleteWishListItem(wishListItemPublicId: String): ApiResult<WishList> {
        return try {
            val response = wishlistApi.deleteWishlistItem(wishListItemPublicId)
            val wishList = WishList(
                wishListItem = response.items.map {
                    WishListItem(
                        id = it.id,
                        productPublicId = it.productPublicId,
                        productName = it.productName,
                        productDescription = it.productDescription,
                        productImage = it.productImage,
                        basePrice = it.basePrice,
                        isAvailable = it.isAvailable,
                        availableSizes = it.availableSizes.map { size ->
                            WishListItemSize(
                                id = size.id,
                                size = size.size,
                                stockQuantity = size.stockQuantity,
                                priceModifier = size.priceModifier,
                                finalPrice = size.finalPrice
                            )
                        },
                        addedAt = it.addedAt
                    )
                }
            )
            ApiResult.Success(wishList)
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