package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreatePaymentMethod
import setixx.software.kaimono.domain.model.PaymentMethod

interface PaymentMethodRepository {
    suspend fun getPaymentMethods(): ApiResult<List<PaymentMethod>>
    suspend fun addPaymentMethod(createPaymentMethod: CreatePaymentMethod): ApiResult<PaymentMethod>
    suspend fun setDefaultPaymentMethod(paymentMethodId: Long): ApiResult<PaymentMethod>
    suspend fun deletePaymentMethod(paymentMethodId: Long): ApiResult<String>
}