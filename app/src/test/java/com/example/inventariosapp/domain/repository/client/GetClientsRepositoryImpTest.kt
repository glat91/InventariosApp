package com.example.inventariosapp.domain.repository.client

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.local.CompanyDatabase
import com.example.inventariosapp.local.entity.ClientEntity
import com.example.inventariosapp.util.NetworkMonitor
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para GetClientsRepositoryImp con base de datos local
 */
class GetClientsRepositoryImpTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getClientsRepositoryImp: GetClientsRepositoryImp
    private val apiService = mockk<ApiService>()
    private val networkMonitor = mockk<NetworkMonitor>()
    private lateinit var db: CompanyDatabase
    
    @Before
    fun setUp() {
        val context = mockk<Context>(relaxed = true)
        db = Room.inMemoryDatabaseBuilder(context, CompanyDatabase::class.java).build()
        // Mockear isConnected para que siempre sea true en las pruebas
        every { networkMonitor.isConnected } returns MutableStateFlow(true)

        getClientsRepositoryImp = GetClientsRepositoryImp(
            apiService,
            db.getClientDao(),
            networkMonitor
        )
    }
    
    @After
    fun tearDown() {
        db.close()
    }
    
    @Test
    fun testGetClientsRepositoryImp_withInternet_success() = runTest {
        // Given
        val internetUse = true
        val mockResponse = listOf(
            ClientResponseModel().apply {
                clienteId = 1
                nombreCliente = "Client 1"
            }
        )
        
        coEvery { apiService.getClient() } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns mockResponse
        }
        
        // When
        val result = getClientsRepositoryImp(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(1, result.first?.size)
        assertNull(result.second)
    }
    
    @Test
    fun testGetClientsRepositoryImp_withoutInternet() = runTest {
        // Given
        val internetUse = false
        
        // Mockear isConnected para que sea false cuando internetUse es false
        every { networkMonitor.isConnected } returns MutableStateFlow(false)

        // Insert mock data in database
        db.getClientDao().insertAll(
            listOf(
                ClientEntity(
                    clienteId = 1,
                    tipoPersonaId = 1,
                    tipoPersona = "persona",
                    tipoGiroId = 1,
                    tipoGiro = "giro",
                    tipoPagoId = 1,
                    tipoPago = "pago",
                    nombreCliente = "Client 1",
                    razonSocial = "razon social",
                    rfc = "RFC123",
                    paisId = 1,
                    estadoId = 1,
                    ciudadId = 1,
                    localidad = "localidad",
                    direccion = "direccion",
                    colonia = "colonia",
                    calle = "calle",
                    codigoPostal = "cp",
                    noExterior = "no ext",
                    noInterior = "no int",
                    telefono = "telefono",
                    correo = "correo@ejemplo.com",
                    montoCredito = 0.0,
                    diasCredito = 0,
                    usuarioSesionId = 1,
                    fechaIngreso = "2024-01-01",
                    curp = "curp",
                    fechaModifico = "",
                    esActivo = true,
                    estatus = "",
                )
            )
        )
        
        // When
        val result = getClientsRepositoryImp(internetUse)
        
        // Then
        assertNotNull(result.first)
        assertEquals(1, result.first?.size)
        assertNull(result.second)
    }
    
    @Test
    fun testGetClientsRepositoryImp_withError() = runTest {
        // Given
        val internetUse = true
        
        coEvery { apiService.getClient() } returns mockk {
            every { isSuccessful } returns false
            every { errorBody()?.string() } returns "{}"
        }
        
        // When
        val result = getClientsRepositoryImp(internetUse)
        
        // Then
        assertNull(result.first)
        assertNotNull(result.second)
    }
}
