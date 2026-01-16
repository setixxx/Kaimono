package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): ApiResult<List<Category>>
    suspend fun getCategoryById(id: Long): ApiResult<Category>
}