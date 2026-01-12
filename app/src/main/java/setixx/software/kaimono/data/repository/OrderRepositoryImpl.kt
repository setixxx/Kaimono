package setixx.software.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import setixx.software.kaimono.data.remote.OrderApi
import setixx.software.kaimono.data.remote.dto.CreateOrderRequest
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.Address
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateOrder
import setixx.software.kaimono.domain.model.DeliveryInfo
import setixx.software.kaimono.domain.model.Order
import setixx.software.kaimono.domain.model.OrderItem
import setixx.software.kaimono.domain.model.PaymentInfo
import setixx.software.kaimono.domain.model.PaymentMethod
import setixx.software.kaimono.domain.model.PaymentMethodInfo
import setixx.software.kaimono.domain.repository.OrderRepository
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.map

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi
) : OrderRepository {
    override suspend fun getOrders(): ApiResult<List<Order>> {
        return try {
            val response = orderApi.getOrders()
            val orders = response.map {
                Order(
                    publicId = it.publicId,
                    status = it.status,
                    totalAmount = it.totalAmount,
                    createdAt = it.createdAt,
                    items = it.items.map { item ->
                        OrderItem(
                            productName = item.productName,
                            size = item.size,
                            quantity = item.quantity,
                            priceAtPurchase = item.priceAtPurchase,
                            subtotal = item.subtotal
                        )
                    },
                    deliveryInfo = it.deliveryInfo?.let { delivery ->
                        DeliveryInfo(
                            trackingNumber = delivery.trackingNumber,
                            status = delivery.status,
                            estimatedDate = delivery.estimatedDate,
                            address = Address(
                                city = delivery.address.city,
                                street = delivery.address.street,
                                house = delivery.address.house,
                                apartment = delivery.address.apartment,
                                code = delivery.address.code,
                                additionalInfo = delivery.address.additionalInfo,
                                isDefault = delivery.address.isDefault,
                                id = delivery.address.id
                            )
                        )
                    },
                    paymentInfo = it.paymentInfo?.let { payment ->
                        PaymentInfo(
                            id = payment.id,
                            amount = payment.amount,
                            status = payment.status,
                            paymentType = payment.paymentType,
                            transactionId = payment.transactionId,
                            paidAt = payment.paidAt,
                            paymentMethod = payment.paymentMethod?.let { method ->
                                PaymentMethodInfo(
                                    cardNumberLast4 = method.cardNumberLast4,
                                    cardHolderName = method.cardHolderName
                                )
                            }
                        )
                    }
                )
            }
            ApiResult.Success(orders)
        } catch (e: HttpException) {
            Log.d("Address", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Address", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun createOrder(createOrder: CreateOrder): ApiResult<Order> {
        return try {
            val request = CreateOrderRequest(
                addressId = createOrder.addressId,
                paymentMethodId = createOrder.paymentMethodId,
                paymentType = createOrder.paymentMethod
            )
            val response = orderApi.createOrder(request)
            ApiResult.Success(
                Order(
                    publicId = response.publicId,
                    status = response.status,
                    totalAmount = response.totalAmount,
                    createdAt = response.createdAt,
                    items = response.items.map { item ->
                        OrderItem(
                            productName = item.productName,
                            size = item.size,
                            quantity = item.quantity,
                            priceAtPurchase = item.priceAtPurchase,
                            subtotal = item.subtotal
                        )
                    },
                    deliveryInfo = response.deliveryInfo?.let { delivery ->
                        DeliveryInfo(
                            trackingNumber = delivery.trackingNumber,
                            status = delivery.status,
                            estimatedDate = delivery.estimatedDate,
                            address = Address(
                                city = delivery.address.city,
                                street = delivery.address.street,
                                house = delivery.address.house,
                                apartment = delivery.address.apartment,
                                code = delivery.address.code,
                                additionalInfo = delivery.address.additionalInfo,
                                isDefault = delivery.address.isDefault,
                                id = delivery.address.id
                            )
                        )
                    },
                    paymentInfo = response.paymentInfo?.let { payment ->
                        PaymentInfo(
                            id = payment.id,
                            amount = payment.amount,
                            status = payment.status,
                            paymentType = payment.paymentType,
                            transactionId = payment.transactionId,
                            paidAt = payment.paidAt,
                            paymentMethod = payment.paymentMethod?.let { method ->
                                PaymentMethodInfo(
                                    cardNumberLast4 = method.cardNumberLast4,
                                    cardHolderName = method.cardHolderName
                                )
                            }
                        )
                    }
                )
            )
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun getOrderById(publicId: String): ApiResult<Order> {
        return try {
            val response = orderApi.getOrderById(publicId)
            ApiResult.Success(
                Order(
                    publicId = response.publicId,
                    status = response.status,
                    totalAmount = response.totalAmount,
                    createdAt = response.createdAt,
                    items = response.items.map { item ->
                        OrderItem(
                            productName = item.productName,
                            size = item.size,
                            quantity = item.quantity,
                            priceAtPurchase = item.priceAtPurchase,
                            subtotal = item.subtotal
                        )
                    },
                    deliveryInfo = response.deliveryInfo?.let { delivery ->
                        DeliveryInfo(
                            trackingNumber = delivery.trackingNumber,
                            status = delivery.status,
                            estimatedDate = delivery.estimatedDate,
                            address = Address(
                                city = delivery.address.city,
                                street = delivery.address.street,
                                house = delivery.address.house,
                                apartment = delivery.address.apartment,
                                code = delivery.address.code,
                                additionalInfo = delivery.address.additionalInfo,
                                isDefault = delivery.address.isDefault,
                                id = delivery.address.id
                            )
                        )
                    },
                    paymentInfo = response.paymentInfo?.let { payment ->
                        PaymentInfo(
                            id = payment.id,
                            amount = payment.amount,
                            status = payment.status,
                            paymentType = payment.paymentType,
                            transactionId = payment.transactionId,
                            paidAt = payment.paidAt,
                            paymentMethod = payment.paymentMethod?.let { method ->
                                PaymentMethodInfo(
                                    cardNumberLast4 = method.cardNumberLast4,
                                    cardHolderName = method.cardHolderName
                                )
                            }
                        )
                    }
                )
            )
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }
}