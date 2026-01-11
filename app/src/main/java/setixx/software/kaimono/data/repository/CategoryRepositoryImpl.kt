package setixx.software.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import setixx.software.kaimono.data.remote.CategoryApi
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Category
import setixx.software.kaimono.domain.repository.CategoryRepository
import java.io.IOException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: CategoryApi
) : CategoryRepository {
    override suspend fun getCategories(): ApiResult<List<Category>> {
        return try {
            val response = categoryApi.getCategories()
            val categories = response.map {
                Category(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    parentId = it.parentId
                )
            }
            ApiResult.Success(categories)
        } catch (e: HttpException) {
            Log.d("Category", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Category", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun getCategoryById(id: Long): ApiResult<Category> {
        return try {
            val response = categoryApi.getCategoryById(id)
            ApiResult.Success(
                Category(
                    id = response.id,
                    name = response.name,
                    description = response.description,
                    parentId = response.parentId
                )
            )
        } catch (e: HttpException) {
            Log.d("Category", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                404 -> DomainError.NotFound
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Category", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }
}