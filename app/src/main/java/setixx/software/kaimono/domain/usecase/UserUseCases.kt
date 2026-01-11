package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.PasswordUpdate
import setixx.software.kaimono.domain.model.UserUpdate
import setixx.software.kaimono.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getCurrentUser()
}

class UpdateUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userUpdate: UserUpdate) =
        userRepository.updateUserInfo(userUpdate)
}

class UpdatePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(updatePassword: PasswordUpdate): ApiResult<String> =
        userRepository.updatePassword(updatePassword)
}