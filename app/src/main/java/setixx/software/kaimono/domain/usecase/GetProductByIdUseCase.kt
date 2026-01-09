package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Product
import setixx.software.kaimono.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(publicId: String): ApiResult<Product> =
        productRepository.getProductById(publicId)
}