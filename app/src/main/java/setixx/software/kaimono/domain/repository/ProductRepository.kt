package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Product
import setixx.software.kaimono.domain.model.ProductList
import setixx.software.kaimono.domain.model.ProductRequest

interface ProductRepository {
    suspend fun getProducts(
        query: String? = null,
        categoryIds: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        inStockOnly: Boolean? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        page: Int? = null,
        pageSize: Int? = null
    ): ApiResult<ProductList>
    suspend fun getProductById(publicId: String): ApiResult<Product>
}