package com.example.inventariosapp.domain.use_case.product

import com.example.inventariosapp.domain.repository.product.GetProductsRepositoryImp
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para GetProductsUseCase
 */
class GetProductsUseCaseTest {
    
    private lateinit var getProductsUseCase: GetProductsUseCase
    private val repositoryImp = mockk<GetProductsRepositoryImp>()
    
    @Before
    fun setUp() {
        getProductsUseCase = GetProductsUseCase(repositoryImp)
    }
    
    @Test
    fun testGetProductsUseCase_invokesRepository_success() = runBlocking {
        // Given
        val internetUse = true
        val mockResponse = listOf(
            com.example.inventariosapp.domain.model.product.ProductsResponseModel()
        )
        
        coEvery { repositoryImp.invoke(internetUse) } returns Pair(mockResponse, null)
        
        // When
        val result = getProductsUseCase(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(1, result.first?.size)
        assertNull(result.second)
    }
    
    @Test
    fun testGetProductsUseCase_withError() = runBlocking {
        // Given
        val internetUse = true
        val errorMsg = "Error al obtener productos"
        
        coEvery { repositoryImp.invoke(internetUse) } returns Pair(null, errorMsg)
        
        // When
        val result = getProductsUseCase(internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals(errorMsg, result.second)
    }
    
    @Test
    fun testGetProductsUseCase_withEmptyList() = runBlocking {
        // Given
        val internetUse = false
        
        coEvery { repositoryImp.invoke(internetUse) } returns Pair(emptyList(), null)
        
        // When
        val result = getProductsUseCase(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(0, result.first?.size)
        assertNull(result.second)
    }
}
