package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreatePaymentMethod
import software.setixx.kaimono.domain.model.PaymentMethod
import software.setixx.kaimono.domain.repository.PaymentMethodRepository
import javax.inject.Inject

class GetPaymentMethodsUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(): ApiResult<List<PaymentMethod>> =
        paymentMethodRepository.getPaymentMethods()
}

class AddPaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(createPaymentMethod: CreatePaymentMethod) =
        paymentMethodRepository.addPaymentMethod(createPaymentMethod)
}

class SetDefaultPaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(paymentMethodId: Long): ApiResult<PaymentMethod> =
        paymentMethodRepository.setDefaultPaymentMethod(paymentMethodId)
}

class DeletePaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(paymentMethodId: Long): ApiResult<String> =
        paymentMethodRepository.deletePaymentMethod(paymentMethodId)
}