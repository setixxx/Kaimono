package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.PasswordUpdate
import software.setixx.kaimono.domain.model.UserUpdate
import software.setixx.kaimono.domain.repository.UserRepository
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