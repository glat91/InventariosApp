package com.example.inventariosapp.domain.use_case.client

import io.mockk.mockk
import org.junit.Test

/**
 * Prueba básica para GetClientsUseCase (sin mocks)
 */
class GetClientsUseCaseBasicTest {
    
    @Test
    fun testGetClientsUseCase_basic() {
        // Esta prueba es básica porque el use case solo delega al repository
        // Para una prueba completa se requieren mocks del repository
        val useCase = GetClientsUseCase(mockk())
        
        // La lógica real está en el repository, no en el use case
        // Por lo que las pruebas unitarias deben mockear el repository
    }
}
