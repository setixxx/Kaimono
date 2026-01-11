package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Product
import setixx.software.kaimono.domain.model.ProductList

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