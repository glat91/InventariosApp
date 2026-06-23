package com.example.inventariosapp.domain.use_case.sales

import android.util.Log
import com.example.inventariosapp.domain.model.sales.SaleProductModel
import com.example.inventariosapp.domain.repository.sales.GetSalesByIdRepositoryImp
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para GetSalesByIdUseCase
 */
class GetSalesByIdUseCaseTest {
    
    private lateinit var getSalesByIdUseCase: GetSalesByIdUseCase
    private val repositoryImp = mockk<GetSalesByIdRepositoryImp>()
    
    @Before
    fun setUp() {
        getSalesByIdUseCase = GetSalesByIdUseCase(repositoryImp)
    }
    
    @Test
    fun testGetSalesByIdUseCase_invokesRepository() = runBlocking {
        // Given
        val salesId = "123"
        val internetUse = true
        val mockResponse = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse(
            ventaId = 1,
            clienteId = 2,
            estatusVentaId = 1,
            nombreCliente = "John Doe",
            folio = "12345",
            subtotal = 100.0,
            descuento = 0.0,
            iva = 0.0,
            retencion = 0.0,
            total = 10.0,
            fechaVenta = "2022-01-01",
            fechaVentaFormato = "01/01/2022",
            sucursalId = 1,
            almacenId = 1,
            usuario = "john",
            montoPagado = 10.0,
            montoPorPagar = 0.0,
            tipoPagoId = 1,
            esFueraDeLinea = false,
            origenId = 1,
            tipoConexionId = 1,
            ventaIdInterno = 1,
            version = 1,
            ventaProductos = listOf(
                SaleProductModel(
                    VentaProductoId = 1,
                    VentaId = 1,
                    ProductoId = 1,
                    Cantidad = 1,
                    PrecioVenta = 10.0,
                    Costo = 10.0,
                    CantidadSolicitada = 1,
                    VentaIdInterno = 1,
                    nombreProducto = "Producto 1",
                    comentarios = "Comentario 1"
                ),
                SaleProductModel(
                    VentaProductoId = 1,
                    VentaId = 1,
                    ProductoId = 1,
                    Cantidad = 1,
                    PrecioVenta = 10.0,
                    Costo = 10.0,
                    CantidadSolicitada = 1,
                    VentaIdInterno = 1,
                    nombreProducto = "Producto 1",
                    comentarios = "Comentario 1"
                ),
                SaleProductModel(
                    VentaProductoId = 1,
                    VentaId = 1,
                    ProductoId = 1,
                    Cantidad = 1,
                    PrecioVenta = 10.0,
                    Costo = 10.0,
                    CantidadSolicitada = 1,
                    VentaIdInterno = 1,
                    nombreProducto = "Producto 1",
                    comentarios = "Comentario 1"
                ),
                SaleProductModel(
                    VentaProductoId = 1,
                    VentaId = 1,
                    ProductoId = 1,
                    Cantidad = 1,
                    PrecioVenta = 10.0,
                    Costo = 10.0,
                    CantidadSolicitada = 1,
                    VentaIdInterno = 1,
                    nombreProducto = "Producto 1",
                    comentarios = "Comentario 1"
                ),
                SaleProductModel(
                    VentaProductoId = 1,
                    VentaId = 1,
                    ProductoId = 1,
                    Cantidad = 1,
                    PrecioVenta = 10.0,
                    Costo = 10.0,
                    CantidadSolicitada = 1,
                    VentaIdInterno = 1,
                    nombreProducto = "Producto 1",
                    comentarios = "Comentario 1"
                )
            ) as ArrayList<SaleProductModel>
        )
        
        coEvery { repositoryImp.invoke(salesId, internetUse) } returns Pair(mockResponse, null)
        
        // When
        val result = getSalesByIdUseCase(salesId, internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(mockResponse, result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testGetSalesByIdUseCase_withError() = runBlocking {
        // Given
        val salesId = "000"
        val internetUse = true
        val errorMsg = "Error al obtener venta"
        
        coEvery { repositoryImp.invoke(salesId, internetUse) } returns Pair(null, errorMsg)
        
        // When
        val result = getSalesByIdUseCase(salesId, internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals(errorMsg, result.second)
    }
    
    @Test
    fun testGetSalesByIdUseCase_withInternetFalse() = runBlocking {
        // Given
        val salesId = ""
        val internetUse = false
        
        coEvery { repositoryImp.invoke(salesId, internetUse) } returns Pair(null, "Modo offline")
        
        // When
        val result = getSalesByIdUseCase(salesId, internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals("Modo offline", result.second)
    }
}
