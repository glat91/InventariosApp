package com.example.inventariosapp.api

import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.domain.model.login.LoginResponseModel
import com.example.inventariosapp.domain.model.payment.PayModel
import com.example.inventariosapp.domain.model.product.InventarioRseponeModel
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.domain.model.sales.GetPaymentResponseModel
import com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse
import com.example.inventariosapp.domain.model.sales.PostSalesModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // region login
    @GET("${Constants.BYNDS}/Api/usuario/{user},{password}")
    suspend fun validateUser(
        @Path("user") user: String,
        @Path("password") password: String
    ): Response<LoginResponseModel>
    // endregion
    // region productos
    @GET("${Constants.BYNDS}/Api/Producto")
    suspend fun getProducts(
        @Query("EsActivo") esActivo: Boolean = true
    ): Response<List<ProductsResponseModel>>

    @GET("${Constants.BYNDS}/Api/Producto/GetByIdData")
    suspend fun getProductId(
        @Query("productoId") productId: Int
    ): Response<ProductIdResponseModel>

    @GET("${Constants.BYNDS}/Api/Inventario")
    suspend fun getInventario(
        @Query("EsActivo") esActivo: Boolean = true
    ): Response<List<InventarioRseponeModel>>
    // endregion


    // region clientes
    @GET("${Constants.BYNDS}/Api/Cliente")
    suspend fun getClient(
        @Query("EsActivo") esActivo: Boolean = true
    ): Response<List<ClientResponseModel>>
    // endregion


    // region payment
    @GET("${Constants.BYNDS}/Api/TipoPago")
    suspend fun getPaymentMethod(
        @Query("EsActivo") esActivo: Boolean = true
    ): Response<List<GetPaymentResponseModel>>

    @GET("${Constants.BYNDS}/Api/VentaPago/{pagoId}")
    suspend fun getPaymentById(
        @Path("pagoId") pagoId: String
    ): Response<GetPaymentResponseModel>

    @GET("${Constants.BYNDS}/Api/VentaPago")
    suspend fun getPayment(
        @Query("VentaID") ventaID: String,
        @Query("EsActivo") esActivo: Boolean = true
    ): Response<List<PayModel>>

    @POST("${Constants.BYNDS}/Api/VentaPago")
    suspend fun setPayment(
        @Body payments: List<NewPayModel>
    ): Response<Unit>

    @DELETE("${Constants.BYNDS}/Api/VentaPago/{pagoId}")
    suspend fun deletePayment(
        @Path("pagoId") pagoId: Int
    ): Response<Unit>
    // endregion


    // region ventas
    @POST("${Constants.BYNDS}/Api/Venta")
    suspend fun postSale(
        @Body sales: List<PostSalesModel>
    ): Response<Unit>

    @GET("${Constants.BYNDS}/Api/Venta/{saleId}")
    suspend fun getSalesById(
        @Path("saleId") saleId: String
    ): Response<GetSalesByIdResponse>

    @PUT("${Constants.BYNDS}/Api/Venta/{ventaId}")
    suspend fun editSale(@Body venta: GetSalesByIdResponse, @Path("ventaId") ventaId: String) : Response<Unit>

    @GET("${Constants.BYNDS}/Api/Venta")
    suspend fun getSalesInProcess(
        @Query("esActivo") esActivo: Boolean = true,
        @Query("EstatusVentaIds") statusSales: String,
        @Query("fechaInicio") startDate: String,
        @Query("fechaFin") endDate: String
    ): Response<List<SalesModel>>

    @GET("${Constants.BYNDS}/Api/Venta")
    suspend fun getPendingSales(
        @Query("esActivo") esActivo: Boolean = true,
        @Query("estatusVentaIds") estatusVentaIds: String = "1,2",
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFin") fechaFin: String
    ): Response<List<SalesModel>>

    @GET("${Constants.BYNDS}/Api/VentaPago")
    suspend fun getSalePayments(
        @Query("VentaId") pagoId: String,
        @Query("EsActivo") esActivo: Boolean = true
    ): Response<List<GetPaymentResponseModel>>
    // endregion
}