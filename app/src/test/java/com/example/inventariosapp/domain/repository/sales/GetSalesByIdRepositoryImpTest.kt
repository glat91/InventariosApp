package com.example.inventariosapp.domain.repository.sales

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.local.CompanyDatabase
import com.example.inventariosapp.session.SessionManager
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/**
 * Pruebas unitarias para GetSalesByIdRepositoryImp con mockeo de APIs
 */
class GetSalesByIdRepositoryImpTest {
    
    private lateinit var getSalesByIdRepositoryImp: GetSalesByIdRepositoryImp
    private val apiService = mockk<ApiService>()
    
    @Before
    fun setUp() {
        getSalesByIdRepositoryImp = GetSalesByIdRepositoryImp(apiService)
    }
    
    @Test
    fun testGetSalesByIdRepositoryImp_withInternet_success() = runBlocking {
        // Given
        val salesId = "123"
        val internetUse = true
        val mockResponse = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse()
        
        coEvery { apiService.getSalesById(salesId) } returns Response.success(mockResponse)
        // When
        val result = getSalesByIdRepositoryImp(salesId, internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(mockResponse, result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testGetSalesByIdRepositoryImp_withInternet_apiError() = runBlocking {
        // Given
        val salesId = "123"
        val internetUse = true
        
        coEvery { apiService.getSalesById(salesId) } returns Response.error(404, "".toResponseBody(null))
        // When
        val result = getSalesByIdRepositoryImp(salesId, internetUse)
        
        // Then
        assertNull(result.first)
        assertNotNull(result.second)
    }
    
    @Test
    fun testGetSalesByIdRepositoryImp_withInternet_networkError() = runBlocking {
        // Given
        val salesId = "123"
        val internetUse = true

        coEvery { apiService.getSalesById(salesId) } throws IOException("Network error")
        // When & Then
        try {
            getSalesByIdRepositoryImp(salesId, internetUse)
            org.junit.Assert.fail("Expected IOException to be thrown")
        }
        catch (e: IOException) {
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun testGetSalesByIdRepositoryImp_withoutInternet() = runBlocking {
        // Given
        val salesId = "123"
        val internetUse = false

        // When
        val result = getSalesByIdRepositoryImp(salesId, internetUse)

        // Then
        assertNull(result.first)
        assertEquals("Modo offline", result.second)
    }
}
