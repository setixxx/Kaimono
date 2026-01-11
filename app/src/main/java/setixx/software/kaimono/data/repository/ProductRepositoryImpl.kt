package setixx.software.kaimono.data.repository

import retrofit2.HttpException
import setixx.software.kaimono.data.remote.ProductApi
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Category
import setixx.software.kaimono.domain.model.Image
import setixx.software.kaimono.domain.model.Product
import setixx.software.kaimono.domain.model.ProductList
import setixx.software.kaimono.domain.model.Size
import setixx.software.kaimono.domain.repository.ProductRepository
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {
    override suspend fun searchProducts(
        query: String?,
        categoryIds: String?,
        minPrice: Int?,
        maxPrice: Int?,
        inStockOnly: Boolean?,
        sortBy: String?,
        sortOrder: String?,
        page: Int?,
        pageSize: Int?
    ): ApiResult<ProductList> {
        return try {
            val response = productApi.getProducts(
                query = query,
                categoryIds = categoryIds,
                minPrice = minPrice?.toDouble(),
                maxPrice = maxPrice?.toDouble(),
                inStockOnly = inStockOnly,
                sortBy = sortBy,
                sortOrder = sortOrder,
                page = page,
                pageSize = pageSize
            )
            val products = response.products
            val productList = ProductList(
                products = products.map { productResponse ->
                    Product(
                        id = productResponse.id,
                        publicId = productResponse.publicId,
                        name = productResponse.name,
                        description = productResponse.description,
                        basePrice = productResponse.basePrice,
                        isAvailable = productResponse.isAvailable,
                        categories = productResponse.categories.map { categoryResponse ->
                            Category(
                                id = categoryResponse.id,
                                name = categoryResponse.name,
                                description = categoryResponse.description,
                                parentId = categoryResponse.parentId
                            )
                        },
                        sizes = productResponse.sizes.map {
                            Size(
                                id = it.id,
                                size = it.size,
                                stockQuantity = it.stockQuantity,
                                priceModifier = it.priceModifier
                            )
                        },
                        images = productResponse.images.map {
                            Image(
                                id = it.id,
                                imageUrl = it.imageUrl,
                                isPrimary = it.isPrimary,
                                displayOrder = it.displayOrder
                            )
                        },
                        averageRating = productResponse.averageRating,
                        reviewCount = productResponse.reviewCount
                    )
                },
                totalCount = response.totalCount,
                page = response.page,
                pageSize = response.pageSize
            )
            ApiResult.Success(productList)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                404 -> DomainError.NotFound
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

    override suspend fun getProductById(publicId: String): ApiResult<Product> {
        return try {
            val response = productApi.getProductById(publicId)
            val product = Product(
                id = response.id,
                publicId = response.publicId,
                name = response.name,
                description = response.description,
                basePrice = response.basePrice,
                isAvailable = response.isAvailable,
                categories = response.categories.map {
                    Category(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        parentId = it.parentId
                    )
                },
                sizes = response.sizes.map {
                    Size(
                        id = it.id,
                        size = it.size,
                        stockQuantity = it.stockQuantity,
                        priceModifier = it.priceModifier
                    )
                },
                images = response.images.map {
                    Image(
                        id = it.id,
                        imageUrl = it.imageUrl,
                        isPrimary = it.isPrimary,
                        displayOrder = it.displayOrder
                    )
                },
                averageRating = response.averageRating,
                reviewCount = response.reviewCount
            )
            ApiResult.Success(product)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                404 -> DomainError.NotFound
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