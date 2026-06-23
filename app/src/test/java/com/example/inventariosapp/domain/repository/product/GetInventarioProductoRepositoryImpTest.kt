package com.example.inventariosapp.domain.repository.product

import android.content.Context
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para GetInventarioProductoRepositoryImp
 */
class GetInventarioProductoRepositoryImpTest {
    
    private lateinit var getInventarioProductoRepositoryImp: GetInventarioProductoRepositoryImp
    private val apiService = mockk<ApiService>()
    private val context = mockk<Context>()
    
    @Before
    fun setUp() {
        getInventarioProductoRepositoryImp = GetInventarioProductoRepositoryImp(apiService, context)
    }
    @Test
    fun testGetInventarioProductoRepositoryImp_withInternet_success() = runTest {
        // Given
        val productId = 123
        val internetUse = true
        val mockResponse = ProductIdResponseModel()
        coEvery { apiService.getProductId(productId) } returns Response.success(mockResponse)
        // When
        val result = getInventarioProductoRepositoryImp(productId, internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(mockResponse, result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testGetInventarioProductoRepositoryImp_withInternet_error() = runBlocking {
        // Given
        val productId = 123
        val internetUse = true
        
        coEvery { apiService.getProductId(productId) } returns Response.error(500, "".toResponseBody())
        // When
        val result = getInventarioProductoRepositoryImp(productId, internetUse)
        
        // Then
        assertNull(result.first)
        assertNotNull(result.second)
    }
    
    @Test
    fun testGetInventarioProductoRepositoryImp_withoutInternet() = runBlocking {
        // Given
        val productId = 123
        val internetUse = false
        
        // When
        val result = getInventarioProductoRepositoryImp(productId, internetUse)
        
        // Then
        assertNull(result.first)
        assertNull(result.second)
    }
}
