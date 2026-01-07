package setixx.software.kaimono.domain.model

data class User(
    val id: String,
    val name: String,
    val surname: String?,
    val phone: String,
    val email: String,
    val birthday: String?,
    val gender: Gender
)

enum class Gender {
    MALE,
    FEMALE
}