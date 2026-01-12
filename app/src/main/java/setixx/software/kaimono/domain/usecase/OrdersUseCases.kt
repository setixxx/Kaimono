package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateOrder
import setixx.software.kaimono.domain.model.Order
import setixx.software.kaimono.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
){
    suspend operator fun invoke(): ApiResult<List<Order>> =
        orderRepository.getOrders()
}

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
){
    suspend operator fun invoke(createOrder: CreateOrder): ApiResult<Order> =
        orderRepository.createOrder(createOrder)
}

class GetOrderByIdUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(publicId: String): ApiResult<Order> =
        orderRepository.getOrderById(publicId)
}