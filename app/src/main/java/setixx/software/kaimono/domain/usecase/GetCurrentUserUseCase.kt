package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getCurrentUser()
}