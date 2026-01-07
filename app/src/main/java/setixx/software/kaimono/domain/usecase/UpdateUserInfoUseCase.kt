package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.UserUpdate
import setixx.software.kaimono.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userUpdate: UserUpdate) =
        userRepository.updateUserInfo(userUpdate)
}