package setixx.software.kaimono.domain.usecase

import setixx.software.kaimono.domain.model.Address
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.SetDefaultAddress
import setixx.software.kaimono.domain.repository.AddressRepository
import javax.inject.Inject

class SetDefaultAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(addressId: Long): ApiResult<Address> =
        addressRepository.setDefaultAddress(addressId)
}