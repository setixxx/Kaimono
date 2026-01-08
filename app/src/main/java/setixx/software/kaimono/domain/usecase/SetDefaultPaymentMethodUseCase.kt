package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.PaymentMethod
import setixx.software.kaimono.domain.repository.PaymentMethodRepository
import javax.inject.Inject

class SetDefaultPaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(paymentMethodId: Long): ApiResult<PaymentMethod> =
        paymentMethodRepository.setDefaultPaymentMethod(paymentMethodId)
}