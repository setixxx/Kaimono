package setixx.software.kaimono.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import setixx.software.kaimono.domain.validation.EmailValidator
import setixx.software.kaimono.domain.validation.NameValidator
import setixx.software.kaimono.domain.validation.PasswordValidator
import setixx.software.kaimono.domain.validation.PhoneValidator
import setixx.software.kaimono.presentation.common.ErrorMapper
import setixx.software.kaimono.presentation.common.ValidationErrorMapper

@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Provides
    @ViewModelScoped
    fun provideErrorMapper(
        @ApplicationContext context: Context
    ): ErrorMapper {
        return ErrorMapper(context)
    }

    @Provides
    @ViewModelScoped
    fun provideValidationErrorMapper(
        @ApplicationContext context: Context
    ): ValidationErrorMapper {
        return ValidationErrorMapper(context)
    }

    @Provides
    @ViewModelScoped
    fun provideEmailValidator(): EmailValidator {
        return EmailValidator()
    }

    @Provides
    @ViewModelScoped
    fun providePhoneValidator(): PhoneValidator {
        return PhoneValidator()
    }

    @Provides
    @ViewModelScoped
    fun providePasswordValidator(): PasswordValidator {
        return PasswordValidator()
    }

    @Provides
    @ViewModelScoped
    fun provideNameValidator(): NameValidator {
        return NameValidator()
    }
}