package com.example.inventariosapp.ui.view.new_sale

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.domain.use_case.client.GetClientsUseCase
import com.example.inventariosapp.domain.repository.product.GetInventarioProductoRepositoryImp as GetInventarioProductoUseCase
import com.example.inventariosapp.domain.use_case.product.GetInventarioUseCase
import com.example.inventariosapp.domain.use_case.product.GetProductsUseCase
import com.example.inventariosapp.domain.use_case.sales.EditSaleUseCase
import com.example.inventariosapp.domain.use_case.sales.GetSalesByIdUseCase
import com.example.inventariosapp.domain.use_case.sales.PostSaleUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para NewSaleViewModel
 */
class NewSaleViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: NewSaleViewModel
    
    // Mocks de use cases y repositorios
    private val getSalesByIdUseCase = mockk<GetSalesByIdUseCase>()
    private val editSaleUseCase = mockk<EditSaleUseCase>()
    private val postSaleUseCase = mockk<PostSaleUseCase>()
    private val getProductsUseCase = mockk<GetProductsUseCase>()
    private val getClientsUseCase = mockk<GetClientsUseCase>()
    private val getInventarioProductoUseCase = mockk<GetInventarioProductoUseCase>()
    private val getInventarioUseCase = mockk<GetInventarioUseCase>()
    private val baseViewModel = mockk<BaseViewModel>()
    
    @Before
    fun setUp() {
        // Configurar dispatchers para pruebas con corutinas
        Dispatchers.setMain(StandardTestDispatcher())
        
        // Mockear comportamiento por defecto
        coEvery { getSalesByIdUseCase.invoke(any(), any()) } returns Pair(mockk(), null)
        coEvery { editSaleUseCase.invoke(any(), any(), any()) } returns Pair(true, null)
        coEvery { postSaleUseCase.invoke(any(), any()) } returns Pair(Unit, null)
        coEvery { getProductsUseCase.invoke(any()) } returns Pair(emptyList(), null)
        coEvery { getClientsUseCase.invoke(any()) } returns Pair(emptyList(), null)
        coEvery { getInventarioProductoUseCase.invoke(any(), any()) } returns Pair(mockk(), null)
        coEvery { getInventarioUseCase.invoke(any()) } returns Pair(emptyList(), null)
        
        coEvery { baseViewModel.isSessionValid() } returns true
        coEvery { baseViewModel.getUsiarioId() } returns 1
    }
    
    @Test
    fun testNewSaleViewModel_creation() = runTest {
        // When
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        // Then
        assertNotNull(viewModel)
    }
    
    @Test
    fun testGetSale_invokesGetSalesByIdUseCase() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        val salesId = "123"
        val internetUse = true
        
        // Setup UI state
        viewModel.updateSale(com.example.inventariosapp.domain.model.sales.SalesModel(folio = salesId))
        
        coEvery { getSalesByIdUseCase.invoke(salesId, internetUse) } returns Pair(mockk(), null)
        
        // When
        runBlocking(Dispatchers.Main) {
            viewModel.getSale()
        }
        
        // Then
        coVerify(exactly = 1) { getSalesByIdUseCase.invoke(salesId, internetUse) }
    }
    
    @Test
    fun testCreateSale_invokesPostSaleUseCase() = runBlocking {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        val internetUse = true
        val sale = com.example.inventariosapp.domain.model.sales.PostSalesModel().apply {
            clienteId = 1
            nombreCliente = "Test Client"
            total = 100.0
            subtotal = 82.0
            iva = 18.0
        }
        
        coEvery { postSaleUseCase.invoke(any(), any()) } returns Pair(Unit, null)
        coEvery { baseViewModel.isSessionValid() } returns true
        coEvery { baseViewModel.getUsiarioId() } returns 1
        
        // Add a product to the sales list
        val product = com.example.inventariosapp.domain.model.sales.PostSaleProductModel().apply {
            productoId = 1
            cantidad = 2
            precioVenta = 50.0
        }
        
        // When
        runBlocking(Dispatchers.Main) {
            viewModel.createSale()
        }
        
        // Then
        verify(exactly = 1) { runBlocking { postSaleUseCase.invoke(any(), any()) } }
    }
    
    @Test
    fun testAddRow_addsProductToSalesList() = runBlocking {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        val product = com.example.inventariosapp.domain.model.product.ProductsResponseModel().apply {
            productoId = 1
            descripcionPresentacion = "Product 1"
            precioVenta1 = 50.0
            costo = 30.0
        }
        
        // When
        runBlocking(Dispatchers.Main) {
            viewModel.addRow(
                product,
                "",
                com.example.inventariosapp.ui.view.new_sale.AddProductUiState()
            )
        }
        
        // Then
        assertEquals(1, viewModel.products.size)
    }
    
    @Test
    fun testDeleteRow_removesProductFromSalesList() = runBlocking {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        val product = com.example.inventariosapp.domain.model.sales.SaleProductModel().apply {
            ProductoId = 1
            PrecioVenta = 50.0
            Cantidad = 2
        }
        
        runBlocking(Dispatchers.Main) {
            viewModel.addRow(
                com.example.inventariosapp.domain.model.product.ProductsResponseModel().apply {
                    productoId = 1
                    descripcionPresentacion = "Product 1"
                    precioVenta1 = 50.0
                    costo = 30.0
                },
                "",
                com.example.inventariosapp.ui.view.new_sale.AddProductUiState()
            )
        }
        
        assertEquals(1, viewModel.products.size)
        
        // When
        runBlocking(Dispatchers.Main) {
            viewModel.deleteRow(product)
        }
        
        // Then
        assertEquals(0, viewModel.products.size)
    }
    
    @Test
    fun testGetClients_invokesGetClientsUseCase() = runBlocking {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        val internetUse = true
        val mockResponse = listOf(
            com.example.inventariosapp.domain.model.client.ClientResponseModel().apply {
                clienteId = 1
                nombreCliente = "Client 1"
            }
        )
        
        coEvery { getClientsUseCase.invoke(internetUse) } returns Pair(mockResponse, null)
        
        // When
        runBlocking(Dispatchers.Main) {
            viewModel.getClients()
        }
        
        // Then
        coVerify(exactly = 1) { getClientsUseCase.invoke(internetUse) }
    }
    
    @Test
    fun testFilterClients_returnsFilteredClients() = runBlocking {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        val clients = listOf(
            com.example.inventariosapp.domain.model.client.ClientResponseModel().apply {
                clienteId = 1
                nombreCliente = "Client 1"
            },
            com.example.inventariosapp.domain.model.client.ClientResponseModel().apply {
                clienteId = 2
                nombreCliente = "Client 2"
            }
        )
        
        viewModel.updateSale(com.example.inventariosapp.domain.model.sales.SalesModel())
        _uiStateField.set(viewModel, NewSaleUiState(clients = clients))
        
        // When
        val result = runBlocking(Dispatchers.Main) {
            viewModel.filterClients()
        }
        
        // Then
        assertEquals(2, result.size)
    }
    
    @Test
    fun testClearSales_clearsAllLists() = runBlocking {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = NewSaleViewModel(
            getSalesByIdUseCase,
            editSaleUseCase,
            postSaleUseCase,
            getProductsUseCase,
            getClientsUseCase,
            getInventarioProductoUseCase,
            getInventarioUseCase,
            baseViewModel,
            mockk()
        )
        
        runBlocking(Dispatchers.Main) {
            viewModel.addRow(
                com.example.inventariosapp.domain.model.product.ProductsResponseModel().apply {
                    productoId = 1
                    descripcionPresentacion = "Product 1"
                    precioVenta1 = 50.0
                    costo = 30.0
                },
                "",
                com.example.inventariosapp.ui.view.new_sale.AddProductUiState()
            )
        }
        
        assertEquals(1, viewModel.products.size)
        
        // When
        runBlocking(Dispatchers.Main) {
            viewModel.clearSales()
        }
        
        // Then
        assertEquals(0, viewModel.products.size)
    }
}

// Helper para acceder al estado privado del ViewModel
private val _uiStateField = NewSaleViewModel::class.java.getDeclaredField("_uiState").apply {
    isAccessible = true
}
