package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateOrder
import setixx.software.kaimono.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(): ApiResult<List<Order>>
    suspend fun createOrder(createOrder: CreateOrder): ApiResult<Order>
    suspend fun getOrderById(publicId: String): ApiResult<Order>
}