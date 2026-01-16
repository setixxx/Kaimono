package software.setixx.kaimono.domain.usecase

import software.setixx.kaimono.domain.model.Address
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateAddress
import software.setixx.kaimono.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(): ApiResult<List<Address>> =
        addressRepository.getAddresses()
}

class AddAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(createAddress: CreateAddress): ApiResult<Address> =
        addressRepository.addAddress(createAddress)
}

class SetDefaultAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(addressId: Long): ApiResult<Address> =
        addressRepository.setDefaultAddress(addressId)
}

class DeleteAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(addressId: Long): ApiResult<String> =
        addressRepository.deleteAddress(addressId)
}