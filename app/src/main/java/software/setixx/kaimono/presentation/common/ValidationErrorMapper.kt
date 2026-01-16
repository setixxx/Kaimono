package software.setixx.kaimono.presentation.common

import android.content.Context
import software.setixx.kaimono.R
import software.setixx.kaimono.domain.validation.ValidationError

class ValidationErrorMapper(private val context: Context) {

    fun mapToMessage(error: ValidationError): String {
        return when (error) {
            ValidationError.EmailEmpty ->
                context.getString(R.string.error_email_empty)

            ValidationError.EmailInvalidFormat ->
                context.getString(R.string.error_email_format)

            ValidationError.PhoneEmpty ->
                context.getString(R.string.error_phone_empty)

            ValidationError.PhoneInvalidFormat ->
                context.getString(R.string.error_phone_format)

            ValidationError.PasswordEmpty ->
                context.getString(R.string.error_password_empty)

            ValidationError.PasswordTooShort ->
                context.getString(R.string.error_password_length)

            ValidationError.PasswordLowComplexity ->
                context.getString(R.string.error_password_complexity)

            ValidationError.ConfirmPasswordEmpty ->
                context.getString(R.string.error_confirm_password_empty)

            ValidationError.PasswordsDoNotMatch ->
                context.getString(R.string.error_passwords_mismatch)

            ValidationError.NameEmpty ->
                context.getString(R.string.error_name_empty)

            ValidationError.NameInvalidFormat ->
                context.getString(R.string.error_name_format)

            ValidationError.SurnameEmpty ->
                context.getString(R.string.error_surname_empty)

            ValidationError.SurnameInvalidFormat ->
                context.getString(R.string.error_surname_format)

            ValidationError.CityEmpty ->
                context.getString(R.string.error_city_empty)

            ValidationError.StreetEmpty ->
                context.getString(R.string.error_street_empty)

            ValidationError.HouseEmpty ->
                context.getString(R.string.error_house_empty)

            ValidationError.ApartmentEmpty ->
                context.getString(R.string.error_apartment_empty)

            ValidationError.ZipCodeEmpty ->
                context.getString(R.string.error_zip_code_empty)

            ValidationError.ZipCodeInvalidFormat ->
                context.getString(R.string.error_zip_code_format)

            ValidationError.CardNumberEmpty ->
                context.getString(R.string.error_card_number_empty)

            ValidationError.CardNumberInvalidFormat ->
                context.getString(R.string.error_card_number_format)

            ValidationError.CardHolderNameEmpty ->
                context.getString(R.string.error_card_holder_empty)

            ValidationError.ExpiryDateEmpty ->
                context.getString(R.string.error_card_date_empty)

            ValidationError.ExpiryDateInvalidFormat ->
                context.getString(R.string.error_card_date_format)

            ValidationError.ExpiryDateExpired ->
                context.getString(R.string.error_card_date_expired)

            ValidationError.CvvEmpty ->
                context.getString(R.string.error_card_cvv_empty)

            ValidationError.CvvInvalidFormat ->
                context.getString(R.string.error_card_cvv_format)
        }
    }
}
