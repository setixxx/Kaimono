package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.Product
import software.setixx.kaimono.domain.model.ProductList
import software.setixx.kaimono.domain.model.ProductRequest
import software.setixx.kaimono.domain.repository.ProductRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(query: ProductRequest): ApiResult<ProductList> =
        productRepository.searchProducts(
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

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(publicId: String): ApiResult<Product> =
        productRepository.getProductById(publicId)
}