package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.Product
import software.setixx.kaimono.domain.model.ProductList

interface ProductRepository {
    suspend fun searchProducts(
        query: String? = null,
        categoryIds: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        inStockOnly: Boolean? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        page: Int? = null,
        pageSize: Int? = null
    ): ApiResult<ProductList>
    suspend fun getProductById(publicId: String): ApiResult<Product>
}