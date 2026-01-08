package setixx.software.kaimono.domain.repository

import setixx.software.kaimono.domain.model.Address
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateAddress

interface AddressRepository {
    suspend fun getAddress(): ApiResult<List<Address>>
    suspend fun addAddress(createAddress: CreateAddress): ApiResult<Address>
    suspend fun setDefaultAddress(addressId: Long): ApiResult<Address>
    suspend fun deleteAddress(addressId: Long): ApiResult<String>
}