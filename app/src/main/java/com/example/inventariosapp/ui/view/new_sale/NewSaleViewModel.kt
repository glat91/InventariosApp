package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.local.entity.ProductEntity
import com.example.inventariosapp.domain.repository.product.GetInventarioProductoRepositoryImp
import com.example.inventariosapp.domain.use_case.client.GetClientsUseCase
import com.example.inventariosapp.domain.use_case.product.GetProductsUseCase
import com.example.inventariosapp.domain.use_case.sales.EditSaleUseCase
import com.example.inventariosapp.domain.use_case.sales.GetSalesByIdUseCase
import com.example.inventariosapp.domain.use_case.sales.PostSaleUseCase
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.domain.model.product.InventarioRseponeModel
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse
import com.example.inventariosapp.domain.model.sales.PostSaleProductModel
import com.example.inventariosapp.domain.model.sales.PostSalesModel
import com.example.inventariosapp.domain.model.sales.SaleProductModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.domain.use_case.product.GetInventarioUseCase
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class NewSaleUiState(
    val sale: SalesModel = SalesModel(),
    val internetUse: Boolean = false,
    val comentarios: String = "",
    val canModifyClient: Boolean = true,

    val expandenSearchBarS: Boolean = false,
    val client: TextFieldValue = TextFieldValue(""),
    val clients: List<ClientResponseModel> = listOf(),
    val opcions: ArrayList<ClientResponseModel> = arrayListOf(),

    val idSale: String = "",
    val saleData: GetSalesByIdResponse = GetSalesByIdResponse(),
    val editStatus: Boolean = false,

    val serviceClientStatus: Boolean = false,

    val dialogProduct: Boolean = false,
    val expandenSearchBarD: Boolean = false,
    val enableSaveBtn: Boolean = true, // Default to true
    val isSubmitting: Boolean = false, // Added explicit submitting state
    var search: TextFieldValue = TextFieldValue(""),
    val inventory: ArrayList<ProductsResponseModel>? = arrayListOf(),
    val filterInventory: ArrayList<ProductsResponseModel> = arrayListOf(),
    val serviceProductMessage: Boolean = false,

    val price: Double = 0.0,
    val quantity: String = "",
    val serchProductId: Int = 0,
    val product: ProductEntity? = null,
    val totalInventory: ProductIdResponseModel = ProductIdResponseModel(),
    val selectedProduct: ProductsResponseModel? = null,
    val inventoryOffline: InventarioRseponeModel? = null,

    val serverPostSale: Boolean = false,
    val newSale: ArrayList<PostSalesModel> = arrayListOf(),
    val newClient: ClientResponseModel? = null,
    val newProducts: ArrayList<PostSaleProductModel> = arrayListOf(),
)

@HiltViewModel
class NewSaleViewModel @Inject constructor(
    private val getSalesByIdUseCase: GetSalesByIdUseCase,
    private val editSaleUseCase: EditSaleUseCase,
    private val postSaleUseCase: PostSaleUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getInventarioProductoUseCase: GetInventarioProductoRepositoryImp,
    private val getInventarioUseCase: GetInventarioUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext private val cnx: android.content.Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewSaleUiState())
    val uiState = _uiState.asStateFlow()

    val products = mutableStateListOf(SaleProductModel())

    // region Sale Data
    fun getSale() {
        _uiState.update { it.copy(canModifyClient = false) }
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            _uiState.update { it.copy(internetUse = internetUse) }
            if (baseViewModel.isSessionValid()) {
                val idSale = _uiState.value.sale.folio.toString()
                _uiState.update { it.copy(idSale = idSale) }
                val r = getSalesByIdUseCase(idSale, internetUse)
                if (r.first != null) {
                    _uiState.update { it.copy(saleData = r.first!!) }
                    products.clear()
                    products.addAll(_uiState.value.saleData.ventaProductos)
                }
            } else {
                baseViewModel.dialogLogin.value = true
            }
        }
    }

    private fun validateProductsWithDb(): Boolean {
        val inventoryList = _uiState.value.inventory
        if (inventoryList.isNullOrEmpty()) {
            MainActivity.mainDialogMsg.value = "Error: venta corrupta favor reiniciar la app, y actualizar offline"
            MainActivity.mainDialog.value = true
            return false
        }
        for (p in products) {
            val dbProduct = inventoryList.find { 
                it.productoId == p.ProductoId || 
                it.descripcionPresentacion.equals(p.nombreProducto, ignoreCase = true) ||
                it.descripcion.equals(p.nombreProducto, ignoreCase = true)
            }
            if (dbProduct == null) {
                MainActivity.mainDialogMsg.value = "Error: El producto '${p.nombreProducto}' no existe en la base de datos interna."
                MainActivity.mainDialog.value = true
                return false
            }
            
            val dbPrices = listOfNotNull(
                dbProduct.precioVenta1,
                dbProduct.precioVenta2?.takeIf { it > 0.001 },
                dbProduct.precioVenta3?.takeIf { it > 0.001 },
                dbProduct.precioVenta4?.takeIf { it > 0.001 }
            )
            
            val selectedPrice = p.PrecioVenta ?: 0.0
            val hasMatchingPrice = dbPrices.any { Math.abs(it - selectedPrice) < 0.001 }
            if (!hasMatchingPrice) {
                MainActivity.mainDialogMsg.value = "Error: El precio ($${selectedPrice}) para el producto '${p.nombreProducto}' no coincide con ningún precio registrado."
                MainActivity.mainDialog.value = true
                return false
            }
        }
        return true
    }

    fun editSale() {
        if (_uiState.value.isSubmitting) return
        _uiState.update { it.copy(isSubmitting = true, enableSaveBtn = false) }
        
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (!validateProductsWithDb()) {
                baseViewModel.hideLoader()
                _uiState.update { it.copy(isSubmitting = false, enableSaveBtn = true) }
                return@launch
            }
            val internetUse = Helpers.isInternetAvailable(cnx)
            val updatedSaleData = _uiState.value.saleData.copy(
                clienteId = _uiState.value.newClient?.clienteId ?: _uiState.value.saleData.clienteId,
                ventaProductos = java.util.ArrayList(products),
                ventaIdInterno = null
            )
            _uiState.update { it.copy(saleData = updatedSaleData) }
            val r = editSaleUseCase(updatedSaleData, _uiState.value.idSale, internetUse)
            if (r.first != null) {
                MainActivity.mainDialogMsg.value = "Venta modificada"
                MainActivity.mainDialog.value = true
                _uiState.update { it.copy(editStatus = true) }
            } else {
                if (r.second != null) {
                    MainActivity.mainDialogMsg.value = r.second!!
                    MainActivity.mainDialog.value = true
                }
            }
            baseViewModel.hideLoader()
            _uiState.update { it.copy(isSubmitting = false, enableSaveBtn = true) }
        }
    }

    fun deleteRow(data: SaleProductModel) {
        products.remove(data)
        var newTotal = BigDecimal.ZERO

        for (p in products) {
            val precio = p.PrecioVenta ?: 0.0
            val cantidad = p.Cantidad ?: 0
            newTotal += (precio * cantidad.toDouble()).toBigDecimal()
        }

        _uiState.update {
            it.copy(
                sale = it.sale.copy(total = newTotal.toDouble()),
                saleData = it.saleData.copy(total = newTotal.toDouble())
            )
        }
    }

    fun addRow(data: ProductsResponseModel, comentarios: String, productState: AddProductUiState) {
        val selectedPrice = _uiState.value.price
        val dbPrices = listOfNotNull(
            data.precioVenta1,
            data.precioVenta2?.takeIf { it > 0.001 },
            data.precioVenta3?.takeIf { it > 0.001 },
            data.precioVenta4?.takeIf { it > 0.001 }
        )

        val hasMatchingPrice = dbPrices.any { Math.abs(it - selectedPrice) < 0.001 }
        if (!hasMatchingPrice) {
            MainActivity.mainDialogMsg.value = "Error: El precio seleccionado no coincide con ninguno de los precios registrados en la base de datos para este producto."
            MainActivity.mainDialog.value = true
            return
        }

        // Validate inventory availability
        val internetUse = Helpers.isInternetAvailable(cnx)
        val requestedQuantity = productState.quantity.toIntOrNull() ?: 0
        if (MainActivity.internetBtn.value && internetUse) {
            val inventoryAvailable = _uiState.value.totalInventory.inventario ?: 0.0
            if (inventoryAvailable <= 0) {
                MainActivity.mainDialogMsg.value = "No hay inventario del producto ${_uiState.value.selectedProduct?.descripcionPresentacion}"
                MainActivity.mainDialog.value = true
                return
            }
            if (requestedQuantity > inventoryAvailable) {
                MainActivity.mainDialogMsg.value = "No hay suficiente inventario del producto. Disponible: $inventoryAvailable"
                MainActivity.mainDialog.value = true
                return
            }
        }

        var newTotal = BigDecimal(0.0)
        products.add(
            SaleProductModel(
                VentaProductoId = 0,
                VentaId = _uiState.value.sale.ventaId,
                ProductoId = data.productoId,
                Cantidad = productState.quantity.toInt(),
                PrecioVenta = selectedPrice,
                Costo = data.costo,
                CantidadSolicitada = productState.quantity.toInt(),
                VentaIdInterno = null,
                Venta = null,
                nombreProducto = data.descripcionPresentacion ?: "",
                comentarios = productState.comentarios
            )
        )
        for (prod in products) {
            newTotal += (prod.PrecioVenta!! * prod.Cantidad!!.toDouble()).toBigDecimal()
        }
        _uiState.update {
            it.copy(
                sale = it.sale.copy(total = newTotal.toDouble()),
                saleData = it.saleData.copy(total = newTotal.toDouble())
            )
        }
    }

    fun getClients() {
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            _uiState.update { it.copy(internetUse = internetUse) }
            val r = getClientsUseCase(internetUse)
            if (r.first != null) {
                _uiState.update { it.copy(clients = r.first!!) }
            }
            _uiState.update { it.copy(serviceClientStatus = true) }
        }
    }

    fun filterClients(): ArrayList<ClientResponseModel> {
        val opcions = if (_uiState.value.client.text.isBlank()) {
            _uiState.update { it.copy(expandenSearchBarS = false) }
            arrayListOf()
        } else {
            if (_uiState.value.newClient == null) {
                if (_uiState.value.saleData.ventaId != null){
                    Log.i("filter____", _uiState.value.client.text)
                    if (_uiState.value.client.text.length > 9){
                        _uiState.update { it.copy(expandenSearchBarS = false) }
                    }
                    else {
                        _uiState.update { it.copy(expandenSearchBarS = true) }
                    }
                }
                else{
                    _uiState.update { it.copy(expandenSearchBarS = true) }
                }
            }
            ArrayList(_uiState.value.clients.filter {
                it.nombreCliente!!.contains(_uiState.value.client.text, ignoreCase = true)
            })
        }
        if (!_uiState.value.saleData.folio.isNullOrEmpty()) {
            _uiState.update { it.copy(expandenSearchBarS = false) }
        }
        _uiState.update { it.copy(opcions = opcions) }
        return opcions
    }
    // endregion

    // region Dialog Products
    fun getFilter(filter: TextFieldValue){
        _uiState.update { it.copy(search = filter) }
        updateExpandenSearchBarD(true)
        val data = _uiState.value.inventory ?: arrayListOf()
        val query = _uiState.value.search.text.trim()
        val filterInventory =
            if (query.length < 3) {
                _uiState.update { it.copy(expandenSearchBarD = false) }
                ArrayList(data)
            } else {
                _uiState.update {
                    it.copy(expandenSearchBarD = query.length < 10)
                }
                ArrayList(data.filter {
                    it.descripcion?.contains(query, ignoreCase = true) == true
                })
            }
        _uiState.update { it.copy(filterInventory = filterInventory) }
    }

    fun getProducts() {
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            _uiState.update { it.copy(internetUse = internetUse) }
            val r = getProductsUseCase(internetUse)
            if (r.first != null) {
                _uiState.update { it.copy(inventory = r.first as ArrayList<ProductsResponseModel>?) }
            }
            _uiState.update { it.copy(serviceProductMessage = true) }
        }
    }

    fun getProductInventario(productId: Int) {
        baseViewModel.showLoader()
        viewModelScope.launch {
            val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            if (internetUse){
                val r = getInventarioProductoUseCase(productId, true)
                if (r.first != null) {
                    _uiState.update { it.copy(totalInventory = r.first!!) }
                }
            }
            else{
                val r2 = getInventarioUseCase(MainActivity.internetBtn.value)
                if (r2.first != null) {
                    val filtro = r2.first!!.firstOrNull { it.productoId == productId }
                    _uiState.update { it.copy(inventoryOffline = filtro) }
                }
            }
            _uiState.update { it.copy(expandenSearchBarD = true, serchProductId = productId) }
            baseViewModel.hideLoader()
        }
    }
    // endregion

    // region New Sale
    fun createSale() {
        if (_uiState.value.isSubmitting) return
        _uiState.update { it.copy(isSubmitting = true, enableSaveBtn = false) }
        
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (!validateProductsWithDb()) {
                baseViewModel.hideLoader()
                _uiState.update { it.copy(isSubmitting = false, enableSaveBtn = true) }
                return@launch
            }
            val usuarioSesionId = baseViewModel.getUsiarioId()
            if (baseViewModel.isSessionValid()) {
                val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
                _uiState.update { it.copy(internetUse = internetUse) }
                val fecha = Helpers.getDateTime().replace(" ", "T")

                var totalSale = 0.0
                val saleProducts = products.map { p ->
                    totalSale += (p.PrecioVenta!! * p.Cantidad!!)
                    PostSaleProductModel(
                        cantidad = p.CantidadSolicitada,
                        productoId = p.ProductoId,
                        precioVenta = p.PrecioVenta,
                        nombreProducto = p.nombreProducto,
                        costo = p.Costo,
                        ventaId = 0,
                        comentarios = p.comentarios
                    )
                }

                val sale = PostSalesModel(
                    clienteId = _uiState.value.newClient!!.clienteId,
                    nombreCliente = _uiState.value.newClient!!.nombreCliente,
                    ventaId = 0,
                    esActivo = true,
                    fechaIngreso = fecha,
                    fechaVenta = fecha,
                    tipoPagoId = 1,
                    direccion = _uiState.value.newClient!!.direccion ?: "",
                    subtotal = 0.0,
                    iva = 0.0,
                    retencion = 0.0,
                    total = totalSale,
                    usuarioSesionId = usuarioSesionId,
                    ventaProductos = saleProducts as ArrayList<PostSaleProductModel>,
                    tipoConexionId = if (internetUse) 1 else 2,
                    origenId = 2
                )
                val r = postSaleUseCase(listOf(sale), MainActivity.internetBtn.value)

                if (r.first != null) {
                    _uiState.update {
                        it.copy(
                            newProducts = saleProducts.toMutableList() as ArrayList<PostSaleProductModel>,
                            newSale = arrayListOf(sale)
                        )
                    }
                    MainActivity.mainDialogMsg.value =
                        if (internetUse) "Venta guardada" else "Venta guardada en modo offline"
                    MainActivity.mainDialog.value = true
                    _uiState.update { it.copy(serverPostSale = true) }
                }
            } else {
                baseViewModel.dialogLogin.value = true
            }
            _uiState.update { it.copy(isSubmitting = false, enableSaveBtn = true) }
            baseViewModel.hideLoader()
        }
    }
    // endregion

    // region Update UiState
    fun updateSale(sale: SalesModel){
        _uiState.update { it.copy(sale = sale) }
    }
    fun updateComentarios(comentarios: String){
        _uiState.update { it.copy(comentarios = comentarios) }
    }
    fun updateClient(client: TextFieldValue){
        _uiState.update { it.copy(client = client) }
    }
    fun updateDialogProduct(dialogProduct: Boolean){
        _uiState.update { it.copy(dialogProduct = dialogProduct) }
    }
    fun updateSearch(search: TextFieldValue){
        _uiState.update { it.copy(search = search) }
    }
    fun updatePrice(price: Double){
        _uiState.update { it.copy(price = price) }
    }
    fun updateQuantity(quantity: String){
        _uiState.update { it.copy(quantity = quantity) }
    }
    fun updateSelectedProduct(selectedProduct: ProductsResponseModel?){
        _uiState.update { it.copy(selectedProduct = selectedProduct) }
    }
    fun updateNewClient(newClient: ClientResponseModel?){
        _uiState.update { it.copy(newClient = newClient) }
    }
    fun updateExpandenSearchBarS(expandenSearchBarS: Boolean){
        _uiState.update { it.copy(expandenSearchBarS = expandenSearchBarS) }
    }
    fun updateEnableSaveBtn(enableSaveBtn: Boolean){
        _uiState.update { it.copy(enableSaveBtn = enableSaveBtn) }
    }
    fun updateExpandenSearchBarD(expandenSearchBarD: Boolean){
        _uiState.update { it.copy(expandenSearchBarD = expandenSearchBarD) }
    }
    fun updateInventoryOffline(inventoryOffline: InventarioRseponeModel?){
        _uiState.update { it.copy(inventoryOffline = inventoryOffline) }
    }
    // endregion

    // region Clean
    fun clearSales() {
        _uiState.update {
            it.copy(
                sale = SalesModel(),
                newSale = arrayListOf(),
                newClient = null,
                newProducts = arrayListOf(),
            )
        }
        products.clear()
    }

    fun clearDialog() {
        _uiState.update {
            it.copy(
                dialogProduct = false,
                search = TextFieldValue(""),
                product = null,
                expandenSearchBarD = false,
                selectedProduct = null,
                quantity = "",
                price = 0.0,
                totalInventory = ProductIdResponseModel()
            )
        }
        _uiState.value.opcions.clear()
    }

    fun clearClient() {
        _uiState.update {
            it.copy(
                client = TextFieldValue(""),
            )
        }
        _uiState.value.opcions.clear()
    }
    // endregion

    init {
        _uiState.update {
            it.copy(internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
        }
        clearSales()
        getProducts()
        getClients()
    }
}
