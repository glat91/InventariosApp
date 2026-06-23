package com.example.inventariosapp.domain.repository.product

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.CompanyDatabase
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
 * Pruebas unitarias para GetProductsRepositoryImp con base de datos local
 */
class GetProductsRepositoryImpTest {
    
    private lateinit var getProductsRepositoryImp: GetProductsRepositoryImp
    private val apiService = mockk<ApiService>()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: CompanyDatabase
    private val productDao = mockk<com.example.inventariosapp.local.dao.ProductDao>()
    
    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, CompanyDatabase::class.java).build()
        
        getProductsRepositoryImp = GetProductsRepositoryImp(
            apiService,
            db.getProductDao(),
            mockk()
        )
    }
    
    @After
    fun tearDown() {
        db.close()
    }
    
    @Test
    fun testGetProductsRepositoryImp_withInternet_success() = runBlocking {
        // Given
        val internetUse = true
        val mockResponse = listOf(
            com.example.inventariosapp.domain.model.product.ProductsResponseModel().apply {
                productoId = 1
                descripcion = "Product 1"
                precioVenta1 = 10.0
            }
        )
        
        coEvery { apiService.getProducts(any()) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns mockResponse
        }
        
        // When
        val result = getProductsRepositoryImp(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(1, result.first?.size)
        assertNull(result.second)
    }
    
    @Test
    fun testGetProductsRepositoryImp_withoutInternet() = runBlocking {
        // Given
        val internetUse = false
        
        // Insert mock data in database
        db.getProductDao().insertAll(
            listOf(
                com.example.inventariosapp.local.entity.ProductEntity().apply {
                    productoId = 1
                    descripcion = "Product 1"
                    precioVenta1 = 10.0
                }
            )
        )
        
        // When
        val result = getProductsRepositoryImp(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(1, result.first?.size)
        assertNull(result.second)
    }
    
    @Test
    fun testGetProductsRepositoryImp_withError() = runBlocking {
        // Given
        val internetUse = true
        
        coEvery { apiService.getProducts(any()) } returns mockk {
            every { isSuccessful } returns false
            every { errorBody()?.string() } returns "{}"
        }
        
        // When
        val result = getProductsRepositoryImp(internetUse)
        
        // Then
        assertNull(result.first)
        assertNotNull(result.second)
    }
}
