package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreatePaymentMethod
import software.setixx.kaimono.domain.model.PaymentMethod

interface PaymentMethodRepository {
    suspend fun getPaymentMethods(): ApiResult<List<PaymentMethod>>
    suspend fun addPaymentMethod(createPaymentMethod: CreatePaymentMethod): ApiResult<PaymentMethod>
    suspend fun setDefaultPaymentMethod(paymentMethodId: Long): ApiResult<PaymentMethod>
    suspend fun deletePaymentMethod(paymentMethodId: Long): ApiResult<String>
}