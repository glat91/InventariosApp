package com.example.inventariosapp.domain.use_case.sales

import com.example.inventariosapp.domain.repository.sales.EditSaleRepositoryImp
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para EditSaleUseCase
 */
class EditSaleUseCaseTest {
    
    private lateinit var editSaleUseCase: EditSaleUseCase
    private val repositoryImp = mockk<EditSaleRepositoryImp>()
    
    @Before
    fun setUp() {
        editSaleUseCase = EditSaleUseCase(repositoryImp)
    }
    
    @Test
    fun testEditSaleUseCase_invokesRepository_success() = runBlocking {
        // Given
        val sale = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse()
        val saleId = "123"
        val internetUse = true
        
        coEvery { repositoryImp.invoke(sale, saleId, internetUse) } returns Pair(true, null)
        
        // When
        val result = editSaleUseCase(sale, saleId, internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(true, result.first)
        assertNull(result.second)
    }
    
    @Test
    fun testEditSaleUseCase_invokesRepository_withError() = runBlocking {
        // Given
        val sale = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse()
        val saleId = "123"
        val internetUse = true
        val errorMsg = "Error al editar venta"
        
        coEvery { repositoryImp.invoke(sale, saleId, internetUse) } returns Pair(null, errorMsg)
        
        // When
        val result = editSaleUseCase(sale, saleId, internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals(errorMsg, result.second)
    }
    
    @Test
    fun testEditSaleUseCase_withInternetFalse() = runBlocking {
        // Given
        val sale = com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse()
        val saleId = "123"
        val internetUse = false
        
        coEvery { repositoryImp.invoke(sale, saleId, internetUse) } returns Pair(null, "No hay conexión a internet")
        
        // When
        val result = editSaleUseCase(sale, saleId, internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals("No hay conexión a internet", result.second)
    }
}
