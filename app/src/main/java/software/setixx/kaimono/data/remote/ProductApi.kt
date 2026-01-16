package software.setixx.kaimono.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import software.setixx.kaimono.data.remote.dto.ProductListResponse
import software.setixx.kaimono.data.remote.dto.ProductResponse

interface ProductApi {
    @GET("/products")
    suspend fun getProducts(
        @Query("query") query: String?,
        @Query("category_ids") categoryIds: String?,
        @Query("min_price") minPrice: Double?,
        @Query("max_price") maxPrice: Double?,
        @Query("in_stock_only") inStockOnly: Boolean?,
        @Query("sort_by") sortBy: String?,
        @Query("sort_order") sortOrder: String?,
        @Query("page") page: Int?,
        @Query("page_size") pageSize: Int?
    ): ProductListResponse
    @GET("/products/{id}")
    suspend fun getProductById(@Path("id") publicId: String): ProductResponse
}