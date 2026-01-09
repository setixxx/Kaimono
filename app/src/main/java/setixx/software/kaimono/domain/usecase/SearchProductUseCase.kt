package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.ProductList
import setixx.software.kaimono.domain.model.ProductRequest
import setixx.software.kaimono.domain.repository.ProductRepository
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(query: ProductRequest): ApiResult<ProductList> =
        productRepository.getProducts(
            query = query.query,
            categoryIds = query.categoryIds,
            minPrice = query.minPrice,
            maxPrice = query.maxPrice,
            inStockOnly = query.inStockOnly,
            sortBy = query.sortBy,
            sortOrder = query.sortOrder,
            page = query.page,
            pageSize = query.pageSize
        )
}