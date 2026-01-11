package setixx.software.kaimono.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import setixx.software.kaimono.data.repository.AddressRepositoryImpl
import setixx.software.kaimono.data.repository.AuthRepositoryImpl
import setixx.software.kaimono.data.repository.CategoryRepositoryImpl
import setixx.software.kaimono.data.repository.PaymentMethodRepositoryImpl
import setixx.software.kaimono.data.repository.ProductRepositoryImpl
import setixx.software.kaimono.data.repository.ReviewRepositoryImpl
import setixx.software.kaimono.data.repository.UserRepositoryImpl
import setixx.software.kaimono.domain.repository.AddressRepository
import setixx.software.kaimono.domain.repository.AuthRepository
import setixx.software.kaimono.domain.repository.CategoryRepository
import setixx.software.kaimono.domain.repository.PaymentMethodRepository
import setixx.software.kaimono.domain.repository.ProductRepository
import setixx.software.kaimono.domain.repository.ReviewRepository
import setixx.software.kaimono.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAddressRepository(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository

    @Binds
    @Singleton
    abstract fun bindPaymentMethodRepository(paymentMethodRepositoryImpl: PaymentMethodRepositoryImpl): PaymentMethodRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository
}