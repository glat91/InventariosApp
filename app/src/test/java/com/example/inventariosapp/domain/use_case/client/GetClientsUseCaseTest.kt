package com.example.inventariosapp.domain.use_case.client

import com.example.inventariosapp.domain.repository.client.GetClientsRepositoryImp
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para GetClientsUseCase
 */
class GetClientsUseCaseTest {
    
    private lateinit var getClientsUseCase: GetClientsUseCase
    private val repositoryImp = mockk<GetClientsRepositoryImp>()
    
    @Before
    fun setUp() {
        getClientsUseCase = GetClientsUseCase(repositoryImp)
    }
    
    @Test
    fun testGetClientsUseCase_invokesRepository_success() = runBlocking {
        // Given
        val internetUse = true
        val mockResponse = listOf(
            com.example.inventariosapp.domain.model.client.ClientResponseModel()
        )
        
        coEvery { repositoryImp.invoke(internetUse) } returns Pair(mockResponse, null)
        
        // When
        val result = getClientsUseCase(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(1, result.first?.size)
        assertNull(result.second)
    }
    
    @Test
    fun testGetClientsUseCase_withError() = runBlocking {
        // Given
        val internetUse = true
        val errorMsg = "Error al obtener clientes"
        
        coEvery { repositoryImp.invoke(internetUse) } returns Pair(null, errorMsg)
        
        // When
        val result = getClientsUseCase(internetUse)
        
        // Then
        assertNull(result.first)
        assertEquals(errorMsg, result.second)
    }
    
    @Test
    fun testGetClientsUseCase_withEmptyList() = runBlocking {
        // Given
        val internetUse = false
        
        coEvery { repositoryImp.invoke(internetUse) } returns Pair(emptyList(), null)
        
        // When
        val result = getClientsUseCase(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(0, result.first?.size)
        assertNull(result.second)
    }
}
