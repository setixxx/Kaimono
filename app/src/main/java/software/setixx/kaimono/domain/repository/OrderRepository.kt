package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateOrder
import software.setixx.kaimono.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(): ApiResult<List<Order>>
    suspend fun createOrder(createOrder: CreateOrder): ApiResult<Order>
    suspend fun getOrderById(publicId: String): ApiResult<Order>
}