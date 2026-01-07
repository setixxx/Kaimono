package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.AuthResult
import setixx.software.kaimono.domain.model.User

interface UserRepository {
    suspend fun getCurrentUser(): AuthResult<User>
}