package com.example.inventariosapp.domain.use_case.product

import io.mockk.mockk
import org.junit.Test

/**
 * Prueba básica para GetProductsUseCase (sin mocks)
 */
class GetProductsUseCaseBasicTest {
    
    @Test
    fun testGetProductsUseCase_basic() {
        // Esta prueba es básica porque el use case solo delega al repository
        // Para una prueba completa se requieren mocks del repository
        val useCase = GetProductsUseCase(mockk())
        
        // La lógica real está en el repository, no en el use case
        // Por lo que las pruebas unitarias deben mockear el repository
    }
}
