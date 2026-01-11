package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): ApiResult<List<Category>>
    suspend fun getCategoryById(id: Long): ApiResult<Category>
}