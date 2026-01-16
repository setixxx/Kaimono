package software.setixx.kaimono.domain.repository

import software.setixx.kaimono.domain.model.Address
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateAddress

interface AddressRepository {
    suspend fun getAddresses(): ApiResult<List<Address>>
    suspend fun addAddress(createAddress: CreateAddress): ApiResult<Address>
    suspend fun setDefaultAddress(addressId: Long): ApiResult<Address>
    suspend fun deleteAddress(addressId: Long): ApiResult<String>
}