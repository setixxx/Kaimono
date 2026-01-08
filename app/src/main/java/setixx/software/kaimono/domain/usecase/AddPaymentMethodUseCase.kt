package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.CreatePaymentMethod
import setixx.software.kaimono.domain.repository.PaymentMethodRepository
import javax.inject.Inject

class AddPaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(createPaymentMethod: CreatePaymentMethod) =
        paymentMethodRepository.addPaymentMethod(createPaymentMethod)
}