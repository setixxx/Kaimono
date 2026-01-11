package setixx.software.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import setixx.software.kaimono.data.remote.PaymentMethodApi
import setixx.software.kaimono.data.remote.dto.CreatePaymentMethodRequest
import setixx.software.kaimono.data.remote.dto.PaymentMethodResponse
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreatePaymentMethod
import setixx.software.kaimono.domain.model.PaymentMethod
import setixx.software.kaimono.domain.repository.PaymentMethodRepository
import java.io.IOException
import javax.inject.Inject

class PaymentMethodRepositoryImpl @Inject constructor(
    private val paymentMethodApi: PaymentMethodApi
) : PaymentMethodRepository {
    override suspend fun getPaymentMethods(): ApiResult<List<PaymentMethod>> {
        return try {
            val response = paymentMethodApi.getPaymentMethods()
            val paymentMethods = response.map {
                PaymentMethod(
                    id = it.id,
                    paymentType = it.paymentType,
                    cardNumberLast4 = it.cardNumberLast4,
                    cardHolderName = it.cardHolderName,
                    expiryMonth = it.expiryMonth,
                    expiryYear = it.expiryYear,
                    cvv = it.cvv,
                    isDefault = it.isDefault
                )
            }
            ApiResult.Success(paymentMethods)
        } catch (e: HttpException) {
            Log.d("Payment", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Payment", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun addPaymentMethod(createPaymentMethod: CreatePaymentMethod): ApiResult<PaymentMethod> {
        return try {
            val request = CreatePaymentMethodRequest(
                cardNumber = createPaymentMethod.cardNumber,
                cardHolderName = createPaymentMethod.cardHolderName,
                expiryMonth = createPaymentMethod.expiryMonth,
                expiryYear = createPaymentMethod.expiryYear,
                cvv = createPaymentMethod.cvv,
                isDefault = createPaymentMethod.isDefault
            )
            val response = paymentMethodApi.createPaymentMethod(request)
            if (!response.isConsistentWith(request)) {
                ApiResult.Error(DomainError.DataInconsistent)
            } else {
                ApiResult.Success(
                    PaymentMethod(
                        id = response.id,
                        paymentType = response.paymentType,
                        cardNumberLast4 = response.cardNumberLast4,
                        cardHolderName = response.cardHolderName,
                        expiryMonth = response.expiryMonth,
                        expiryYear = response.expiryYear,
                        cvv = response.cvv,
                        isDefault = response.isDefault
                    )
                )
            }
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
            Log.d("Payment", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun setDefaultPaymentMethod(paymentMethodId: Long): ApiResult<PaymentMethod> {
        return try {
            val response = paymentMethodApi.setDefaultPaymentMethod(paymentMethodId)
            ApiResult.Success(
                PaymentMethod(
                    id = response.id,
                    paymentType = response.paymentType,
                    cardNumberLast4 = response.cardNumberLast4,
                    cardHolderName = response.cardHolderName,
                    expiryMonth = response.expiryMonth,
                    expiryYear = response.expiryYear,
                    cvv = response.cvv,
                    isDefault = response.isDefault
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

    override suspend fun deletePaymentMethod(paymentMethodId: Long): ApiResult<String> {
        return try {
            val response = paymentMethodApi.deletePaymentMethod(paymentMethodId)
            ApiResult.Success(response.message)
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
private fun PaymentMethodResponse.isConsistentWith(request: CreatePaymentMethodRequest): Boolean{
    return cardNumberLast4 == request.cardNumber.takeLast(4) &&
            cardHolderName == request.cardHolderName &&
            expiryMonth == request.expiryMonth &&
            expiryYear == request.expiryYear
}