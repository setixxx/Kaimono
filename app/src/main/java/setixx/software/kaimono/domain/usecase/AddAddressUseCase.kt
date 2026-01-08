package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.Address
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateAddress
import setixx.software.kaimono.domain.repository.AddressRepository
import javax.inject.Inject

class AddAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(createAddress: CreateAddress): ApiResult<Address> =
        addressRepository.addAddress(createAddress)
}