package setixx.software.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import setixx.software.kaimono.data.remote.dto.AddressResponse
import setixx.software.kaimono.data.remote.dto.CreateAddressRequest
import setixx.software.kaimono.data.remote.dto.DeleteAddressRequest
import setixx.software.kaimono.data.remote.dto.DeleteAddressResponse
import setixx.software.kaimono.data.remote.dto.SetDefaultAddressRequest

interface AddressApi {
    @GET("/addresses")
    suspend fun getAllAddresses(): List<AddressResponse>

    @POST("/addresses")
    suspend fun createAddress(@Body createAddressRequest: CreateAddressRequest): AddressResponse

    @PATCH("/addresses/{id}/set-default")
    suspend fun setDefaultAddress(@Path("id") addressId: Long): AddressResponse

    @DELETE("/addresses/{id}")
    suspend fun deleteAddress(@Path("id") addressId: Long): DeleteAddressResponse
}
