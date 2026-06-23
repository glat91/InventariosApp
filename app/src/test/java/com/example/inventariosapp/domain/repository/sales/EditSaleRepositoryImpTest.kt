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
 * Pruebas unitarias para EditSaleRepositoryImp con mockeo de APIs y sesión
 */
class EditSaleRepositoryImpTest {

    private lateinit var editSaleRepositoryImp: EditSaleRepositoryImp
    private val sessionManager = mockk<SessionManager>()
    private val apiService = mockk<ApiService>()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: CompanyDatabase

    @Before
    fun setUp() {
        // Mockear isSessionValid como suspend function
        coEvery { sessionManager.isSessionValid() } returns true

        editSaleRepositoryImp = EditSaleRepositoryImp(sessionManager, apiService)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testEditSaleRepositoryImp_withInternet_sessionValid_success() = runBlocking {
        // Given
        val sale = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse()
        val saleId = "123"
        val internetUse = true
        
        coEvery { apiService.editSale(sale, saleId) } returns mockk {
            every { isSuccessful } returns true
        }
        
        // When
        val result = editSaleRepositoryImp(sale, saleId, internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(true, result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testEditSaleRepositoryImp_withInternet_sessionExpired() = runBlocking {
        // Given
        val sale = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse()
        val saleId = "123"
        val internetUse = true
        
        coEvery { sessionManager.isSessionValid() } returns false

        // When
        val result = editSaleRepositoryImp(sale, saleId, internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals("Sesión expirada, favor de iniciar sesión", result.second)
    }
    
    @Test
    fun testEditSaleRepositoryImp_withoutInternet() = runBlocking {
        // Given
        val sale = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse()
        val saleId = "123"
        val internetUse = false
        
        // When
        val result = editSaleRepositoryImp(sale, saleId, internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals("No hay conexión a internet", result.second)
    }
}
