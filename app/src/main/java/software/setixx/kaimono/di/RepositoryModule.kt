package software.setixx.kaimono.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.setixx.kaimono.data.repository.AddressRepositoryImpl
import software.setixx.kaimono.data.repository.AuthRepositoryImpl
import software.setixx.kaimono.data.repository.CartRepositoryImpl
import software.setixx.kaimono.data.repository.CategoryRepositoryImpl
import software.setixx.kaimono.data.repository.OrderRepositoryImpl
import software.setixx.kaimono.data.repository.PaymentMethodRepositoryImpl
import software.setixx.kaimono.data.repository.ProductRepositoryImpl
import software.setixx.kaimono.data.repository.ReviewRepositoryImpl
import software.setixx.kaimono.data.repository.UserRepositoryImpl
import software.setixx.kaimono.data.repository.WishlistRepositoryImpl
import software.setixx.kaimono.domain.repository.AddressRepository
import software.setixx.kaimono.domain.repository.AuthRepository
import software.setixx.kaimono.domain.repository.CartRepository
import software.setixx.kaimono.domain.repository.CategoryRepository
import software.setixx.kaimono.domain.repository.OrderRepository
import software.setixx.kaimono.domain.repository.PaymentMethodRepository
import software.setixx.kaimono.domain.repository.ProductRepository
import software.setixx.kaimono.domain.repository.ReviewRepository
import software.setixx.kaimono.domain.repository.UserRepository
import software.setixx.kaimono.domain.repository.WishlistRepository
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

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(wishlistRepositoryImpl: WishlistRepositoryImpl): WishlistRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository
}