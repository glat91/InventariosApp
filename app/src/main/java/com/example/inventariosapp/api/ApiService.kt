package com.example.inventariosapp.api

import com.example.inventariosapp.model.client.ClientResponseModel
import com.example.inventariosapp.model.login.LoginResponseModel
import com.example.inventariosapp.model.payment.PayModel
import com.example.inventariosapp.model.product.InventarioRseponeModel
import com.example.inventariosapp.model.product.ProductIdResponseModel
import com.example.inventariosapp.model.product.ProductsResponseModel
import com.example.inventariosapp.model.sales.GetPaymentResponseModel
import com.example.inventariosapp.model.sales.GetSalesByIdResponse
import com.example.inventariosapp.model.sales.PostSalesModel
import com.example.inventariosapp.model.sales.SalesModel
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
    @GET("InventariosApi.QA/Api/usuario/{user},{password}")
    suspend fun validateUser(@Path("user") user: String, @Path("password") passsword: String): Response<LoginResponseModel>
    // endregion
    // region productos
    @GET("InventariosApi.QA/Api/Producto?EsActivo=true")
    suspend fun getProducts(): Response<List<ProductsResponseModel>>

    @GET("InventariosApi.QA/Api/Producto/GetByIdData")
    suspend fun getProductId(@Query("productoId") productId: Int): Response<ProductIdResponseModel>

    @GET("InventariosApi.QA/Api/Inventario?EsActivo=true")
    suspend fun getInventario(): Response<List<InventarioRseponeModel>>
    // endregion
    // region clientes
    @GET("InventariosApi.QA/Api/Cliente?EsActivo=true")
    suspend fun getClient(): Response<List<ClientResponseModel>>
    // endregion
    // region payment
    @GET("InventariosApi.QA/Api/TipoPago?EsActivo=true")
    suspend fun getPaymentMethod(): Response<List<GetPaymentResponseModel>>

    @GET("InventariosApi.QA/Api/VentaPago/{pagoId}")
    suspend fun getPaymentById(pagoId: String): Response<GetPaymentResponseModel>

    @GET("InventariosApi.QA/Api/VentaPago")
    suspend fun getPayment(
        @Query("VentaID") ventaID: String,
        @Query("EsActivo") esActivo: Boolean = true
    ): Response<ArrayList<PayModel>>

    @POST("InventariosApi.QA/Api/VentaPago")
    suspend fun setPayment(
        @Query("VentaID") ventaID: String,
        @Query("montoPago") montoPago: String,
        @Query("fecha") fecha: String,
        @Query("observaciones") observaciones: String,
        @Query("origenId") origenId: Int,
        @Query("tipoConexionId") tipoConexionId: Int,
        @Query("usuarioSesionId") usuarioSesionId: Int
    ): Response<Unit>

    @DELETE("InventariosApi.QA/Api/VentaPago/{pagoId}")
    suspend fun deletePayment(@Path("ventaID") ventaID: Int): Response<Unit>
    // endregion
    // region ventas
    @POST("InventariosApi.QA/Api/Venta")
    suspend fun postSale(@Body sales: ArrayList<PostSalesModel>): Response<Unit>

    @GET("InventariosApi.QA/Api/Venta/{saleId}")
    suspend fun getSalesById(@Path("saleId") saleId: String): Response<GetSalesByIdResponse>

    @PUT("InventariosApi.QA/Api/Venta/{ventaId}")
    suspend fun editSale(@Body venta: GetSalesByIdResponse, @Path("ventaId") ventaId: String) : Response<Unit>

    @GET("InventariosApi.QA/Api/Venta?esActivo=true&EstatusVentaIds={statusSales}&fechaInicio={startDate}&fechaFin={endDate}")
    suspend fun getSalesInProcess(startDate: String, endDate: String): Response<List<SalesModel>>

    @GET("InventariosApi.QA/Api/Venta")
    suspend fun getPendingSales(
        @Query("esActivo") esActivo: Boolean = true,
        @Query("EstatusVentaIds") estatusVentaIds: String = "1,2",
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFin") fechaFin: String
    ): Response<ArrayList<SalesModel>>

    @GET("InventariosApi.QA/Api/VentaPago?VentaId={pagoId}&EsActivo=True")
    suspend fun getSalePayments(pagoId: String): Response<List<GetPaymentResponseModel>>
    // endregion
}