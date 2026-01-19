package software.setixx.kaimono.data.repository

import android.util.Log
import retrofit2.HttpException
import software.setixx.kaimono.data.remote.AddressApi
import software.setixx.kaimono.data.remote.dto.AddressResponse
import software.setixx.kaimono.data.remote.dto.CreateAddressRequest
import software.setixx.kaimono.domain.error.DomainError
import software.setixx.kaimono.domain.model.Address
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.CreateAddress
import software.setixx.kaimono.domain.repository.AddressRepository
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
            val error = when (e.code()) {
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
