package setixx.software.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import setixx.software.kaimono.data.remote.WishlistApi
import setixx.software.kaimono.data.remote.dto.AddToWishlistRequest
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.AddWishListItem
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.WishList
import setixx.software.kaimono.domain.model.WishListItem
import setixx.software.kaimono.domain.model.WishListItemSize
import setixx.software.kaimono.domain.repository.WishlistRepository
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