package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Orders
import setixx.software.kaimono.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository
){
    suspend operator fun invoke(): ApiResult<List<Orders>> =
        ordersRepository.getOrders()
}