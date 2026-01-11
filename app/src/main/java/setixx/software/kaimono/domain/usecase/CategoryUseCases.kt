package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Category
import setixx.software.kaimono.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): ApiResult<List<Category>> {
        return categoryRepository.getCategories()
    }
}

class GetCategoryByIdUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
){
    suspend operator fun invoke(id: Long): ApiResult<Category> {
        return categoryRepository.getCategoryById(id)
    }
}