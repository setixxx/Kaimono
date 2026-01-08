package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.refreshAccessToken()
}