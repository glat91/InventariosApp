package com.example.inventariosapp.domain.use_case.product

import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.error.MsgErrorModel
import com.example.inventariosapp.domain.repository.product.GetInventarioRepositoryImp
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para GetInventarioUseCase
 */
class GetInventarioUseCaseTest {
    private lateinit var getInventarioUseCase: GetInventarioUseCase
    private val repositoryImp = mockk<GetInventarioRepositoryImp>()
    
    @Before
    fun setUp() {
        getInventarioUseCase = GetInventarioUseCase(repositoryImp)
    }
    
    @Test
    fun testGetInventarioUseCase_invokesRepository_success() = runBlocking {
        // Given
        val refresh = true
        val mockResponse = listOf(
            com.example.inventariosapp.domain.model.product.InventarioRseponeModel()
        )
        
        coEvery { repositoryImp.invoke(refresh) } returns Pair(mockResponse, null)
        
        // When
        val result = getInventarioUseCase(refresh)
        
        // Then
        assertNotNull(result.first)
        assertEquals(1, result.first?.size)
        assertNull(result.second)
    }
    
    @Test
    fun testGetInventarioUseCase_withError() = runBlocking {
        // Given
        val refresh = true
        val errorMsg = "Error al obtener inventario"
        
        coEvery { repositoryImp.invoke(refresh) } returns Pair(null, ErrorModel(MsgErrorModel(rawValue = errorMsg)))

        // When
        val result = getInventarioUseCase(refresh)
        
        // Then
        assertNull(result.first)
        assertNotNull(result.second)
    }

    @Test
    fun testGetInventarioUseCase_withEmptyList() = runBlocking {
        // Given
        val refresh = false
        
        coEvery { repositoryImp.invoke(refresh) } returns Pair(emptyList(), null)
        
        // When
        val result = getInventarioUseCase(refresh)
        
        // Then
        assertNotNull(result.first)
        assertEquals(0, result.first?.size)
        assertNull(result.second)
    }
}
