package setixx.software.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import setixx.software.kaimono.data.remote.AddressApi
import setixx.software.kaimono.data.remote.dto.AddressResponse
import setixx.software.kaimono.data.remote.dto.CreateAddressRequest
import setixx.software.kaimono.domain.error.DomainError
import setixx.software.kaimono.domain.model.Address
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.CreateAddress
import setixx.software.kaimono.domain.repository.AddressRepository
import java.io.IOException
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressApi: AddressApi
) : AddressRepository {
    override suspend fun getAddresses(): ApiResult<List<Address>> {
        return try {
            val response = addressApi.getAllAddresses()
            val addresses = response.map {
                Address(
                    id = it.id,
                    city = it.city,
                    street = it.street,
                    house = it.house,
                    apartment = it.apartment,
                    code = it.code,
                    additionalInfo = it.additionalInfo,
                    isDefault = it.isDefault
                )
            }
            ApiResult.Success(addresses)
        } catch (e: HttpException) {
            Log.d("Address", e.message())
            val error = when (e.code()) {
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            Log.d("Address", e.message.toString())
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun addAddress(createAddress: CreateAddress): ApiResult<Address> {
        return try {
            val request = CreateAddressRequest(
                city = createAddress.city,
                street = createAddress.street,
                house = createAddress.house,
                apartment = createAddress.apartment,
                code = createAddress.zipCode,
                additionalInfo = createAddress.additionalInfo,
                isDefault = createAddress.isDefault
            )
            val response = addressApi.createAddress(request)
            if (!response.isConsistentWith(request)) {
                ApiResult.Error(DomainError.DataInconsistent)
            } else {
                ApiResult.Success(
                    Address(
                        id = response.id,
                        city = response.city,
                        street = response.street,
                        house = response.house,
                        apartment = response.apartment,
                        code = response.code,
                        additionalInfo = response.additionalInfo,
                        isDefault = response.isDefault
                    )
                )
            }
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun setDefaultAddress(addressId: Long): ApiResult<Address> {
        return try {
            val response = addressApi.setDefaultAddress(addressId)
            ApiResult.Success(
                Address(
                    id = response.id,
                    city = response.city,
                    street = response.street,
                    house = response.house,
                    apartment = response.apartment,
                    code = response.code,
                    additionalInfo = response.additionalInfo,
                    isDefault = response.isDefault
                )
            )
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }

    override suspend fun deleteAddress(addressId: Long): ApiResult<String> {
        return try {
            val response = addressApi.deleteAddress(addressId)
            ApiResult.Success(response.message)
        } catch (e: HttpException) {
            val error = when (e.code()) {
                400 -> DomainError.InvalidData
                401 -> DomainError.InvalidToken
                500 -> DomainError.ServerInternal
                else -> DomainError.HttpError(e.code(), e.message())
            }
            ApiResult.Error(error)
        } catch (e: IOException) {
            ApiResult.Error(DomainError.NoInternet)
        } catch (e: Exception) {
            ApiResult.Error(DomainError.Unknown(e.message))
        }
    }
}

private fun AddressResponse.isConsistentWith(request: CreateAddressRequest): Boolean {
    return city == request.city &&
            street == request.street &&
            house == request.house &&
            apartment == request.apartment &&
            code == request.code &&
            additionalInfo == request.additionalInfo &&
            isDefault == request.isDefault
}
