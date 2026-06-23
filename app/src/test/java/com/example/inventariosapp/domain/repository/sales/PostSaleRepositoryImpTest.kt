package com.example.inventariosapp.domain.repository.sales

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.CompanyDatabase
import com.example.inventariosapp.session.SessionManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para PostSaleRepositoryImp con base de datos local
 */
class PostSaleRepositoryImpTest {
    
    private lateinit var postSaleRepositoryImp: PostSaleRepositoryImp
    private val sessionManager = mockk<SessionManager>()
    private val apiService = mockk<ApiService>()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: CompanyDatabase
    
    @Before
    fun setUp() {
        // Mockear isSessionValid como suspend function
        coEvery { sessionManager.isSessionValid() } returns true

        postSaleRepositoryImp = PostSaleRepositoryImp(
            sessionManager,
            apiService,
            db.postSales(),
            mockk()
        )
    }
    
    @After
    fun tearDown() {
        db.close()
    }
    
    @Test
    fun testPostSaleRepositoryImp_withInternet_sessionValid_success() = runBlocking {
        // Given
        val sales = listOf(
            com.example.inventariosapp.domain.model.sales.PostSalesModel()
        )
        
        coEvery { apiService.postSale(any()) } returns mockk {
            every { isSuccessful } returns true
        }
        
        // When
        val result = postSaleRepositoryImp(sales, true)
        
        // Then
        assertNotNull(result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testPostSaleRepositoryImp_withInternet_sessionExpired() = runBlocking {
        // Given
        val sales = listOf(
            com.example.inventariosapp.domain.model.sales.PostSalesModel()
        )
        val internetUse = true
        
        coEvery { sessionManager.isSessionValid() } returns false

        // When
        val result = postSaleRepositoryImp(sales, internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals("Sesión expirada, favor de iniciar sesión", result.second)
    }
    
    @Test
    fun testPostSaleRepositoryImp_withoutInternet_savesToLocalDatabase() = runBlocking {
        // Given
        val sales = listOf(
            com.example.inventariosapp.domain.model.sales.PostSalesModel().apply {
                this.ventaId = 0
                this.total = 100.0
                this.subtotal = 82.0
                this.iva = 18.0
                this.nombreCliente = "Test Client"
            }
        )
        
        // When
        val result = postSaleRepositoryImp(sales, false)
        
        // Then
        assertNotNull(result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testPostSaleRepositoryImp_withError() = runBlocking {
        // Given
        val sales = listOf(
            com.example.inventariosapp.domain.model.sales.PostSalesModel()
        )
        
        coEvery { apiService.postSale(any()) } returns mockk {
            every { isSuccessful } returns false
            every { errorBody()?.string() } returns "{}"
        }
        
        // When
        val result = postSaleRepositoryImp(sales, true)
        
        // Then
        assertNull(result.first)
        assertNotNull(result.second)
    }
}
