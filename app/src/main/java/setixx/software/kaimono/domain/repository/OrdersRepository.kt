package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateOrder
import setixx.software.kaimono.domain.model.Orders

interface OrdersRepository {
    suspend fun getOrders(): ApiResult<List<Orders>>
    suspend fun createOrder(createOrder: CreateOrder): ApiResult<Orders>
    suspend fun getOrderById(publicId: String): ApiResult<Orders>
}