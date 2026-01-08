package setixx.software.kaimono.domain.validation

sealed class ValidationError {
    data object EmailEmpty : ValidationError()
    data object EmailInvalidFormat : ValidationError()

    data object PhoneEmpty : ValidationError()
    data object PhoneInvalidFormat : ValidationError()

    data object PasswordEmpty : ValidationError()
    data object PasswordTooShort : ValidationError()
    data object PasswordLowComplexity : ValidationError()

    data object ConfirmPasswordEmpty : ValidationError()
    data object PasswordsDoNotMatch : ValidationError()

    data object NameEmpty : ValidationError()
    data object NameInvalidFormat : ValidationError()

    data object SurnameEmpty : ValidationError()
    data object SurnameInvalidFormat : ValidationError()

    data object CityEmpty : ValidationError()
    data object StreetEmpty : ValidationError()
    data object HouseEmpty : ValidationError()
    data object ApartmentEmpty : ValidationError()
    data object ZipCodeEmpty : ValidationError()
    data object ZipCodeInvalidFormat : ValidationError()
}