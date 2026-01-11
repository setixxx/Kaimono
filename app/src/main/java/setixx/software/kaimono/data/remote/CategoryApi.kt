package setixx.software.kaimono.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import setixx.software.kaimono.data.remote.dto.CategoryResponse

interface CategoryApi {
    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>

    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Long): CategoryResponse
}