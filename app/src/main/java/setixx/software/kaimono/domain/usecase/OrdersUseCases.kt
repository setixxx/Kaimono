package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateOrder
import setixx.software.kaimono.domain.model.Orders
import setixx.software.kaimono.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository
){
    suspend operator fun invoke(): ApiResult<List<Orders>> =
        ordersRepository.getOrders()
}

class CreateOrderUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository
){
    suspend operator fun invoke(createOrder: CreateOrder): ApiResult<Orders> =
        ordersRepository.createOrder(createOrder)
}

class GetOrderByIdUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository
) {
    suspend operator fun invoke(publicId: String): ApiResult<Orders> =
        ordersRepository.getOrderById(publicId)
}