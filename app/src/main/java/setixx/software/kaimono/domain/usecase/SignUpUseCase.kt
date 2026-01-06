package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, phone: String, password: String) =
        authRepository.signUp(email, phone, password)
}