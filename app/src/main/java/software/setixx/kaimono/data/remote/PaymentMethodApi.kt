package software.setixx.kaimono.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import software.setixx.kaimono.data.remote.dto.CreatePaymentMethodRequest
import software.setixx.kaimono.data.remote.dto.DeletePaymentMethodResponse
import software.setixx.kaimono.data.remote.dto.PaymentMethodResponse

interface PaymentMethodApi {
    @GET("/payment-methods")
    suspend fun getPaymentMethods(): List<PaymentMethodResponse>
    @POST("/payment-methods")
    suspend fun createPaymentMethod(@Body createPaymentMethodRequest: CreatePaymentMethodRequest): PaymentMethodResponse
    @PATCH("/payment-methods/{id}/set-default")
    suspend fun setDefaultPaymentMethod(@Path("id") paymentMethodId: Long): PaymentMethodResponse
    @DELETE("/payment-methods/{id}")
    suspend fun deletePaymentMethod(@Path("id") paymentMethodId: Long): DeletePaymentMethodResponse
}