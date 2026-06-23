package com.example.inventariosapp.domain.use_case.sales

import com.example.inventariosapp.domain.repository.sales.PostSaleRepositoryImp
import com.example.inventariosapp.domain.model.sales.PostSalesModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para PostSaleUseCase
 */
class PostSaleUseCaseTest {
    
    private lateinit var postSaleUseCase: PostSaleUseCase
    private val repositoryImp = mockk<PostSaleRepositoryImp>()
    
    @Before
    fun setUp() {
        postSaleUseCase = PostSaleUseCase(repositoryImp)
    }
    
    @Test
    fun testPostSaleUseCase_invokesRepository_success() = runBlocking {
        // Given
        val sales = listOf(PostSalesModel())
        val updateSales = true
        
        coEvery { repositoryImp.invoke(sales, updateSales) } returns Pair(Unit, null)
        
        // When
        val result = postSaleUseCase(sales, updateSales)
        
        // Then
        assertNotNull(result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testPostSaleUseCase_withError() = runBlocking {
        // Given
        val sales = listOf(PostSalesModel())
        val updateSales = true
        val errorMsg = "Error al guardar venta"
        
        coEvery { repositoryImp.invoke(sales, updateSales) } returns Pair(null, errorMsg)
        
        // When
        val result = postSaleUseCase(sales, updateSales)
        
        // Then
        assertNull(result.first)
        assertEquals(errorMsg, result.second)
    }
    
    @Test
    fun testPostSaleUseCase_withNullSales() = runBlocking {
        // Given
        val sales: List<PostSalesModel>? = null
        val updateSales = false
        
        coEvery { repositoryImp.invoke(sales, updateSales) } returns Pair(Unit, null)
        
        // When
        val result = postSaleUseCase(sales, updateSales)
        
        // Then
        assertNotNull(result.first)
        assertNull(result.second)
    }
}
