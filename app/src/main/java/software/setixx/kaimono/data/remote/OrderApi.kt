package software.setixx.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import software.setixx.kaimono.data.remote.dto.CreateOrderRequest
import software.setixx.kaimono.data.remote.dto.OrderResponse

interface OrderApi {
    @GET("/orders")
    suspend fun getOrders(): List<OrderResponse>
    @POST("/orders")
    suspend fun createOrder(@Body createOrderRequest: CreateOrderRequest): OrderResponse
    @GET("/orders/{publicId}")
    suspend fun getOrderById(@Path("publicId") publicId: String): OrderResponse
}