package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateOrder
import software.setixx.kaimono.domain.model.Order
import software.setixx.kaimono.domain.repository.OrderRepository
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