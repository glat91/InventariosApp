package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.domain.model.sales.GetSalesByIdResponse
import com.example.inventariosapp.domain.model.sales.PostSaleProductModel
import com.example.inventariosapp.domain.model.sales.PostSalesModel
import com.example.inventariosapp.domain.model.sales.SaleProductModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class NewSaleViewModel @Inject constructor(
    private val getSalesByIdUseCase: GetSalesByIdUseCase,
    private val editSaleUseCase: EditSaleUseCase,
    private val postSaleUseCase: PostSaleUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getInventarioProductoUseCase: GetInventarioProductoRepositoryImp,
    val baseViewModel: BaseViewModel,
    @ApplicationContext private val cnx : android.content.Context
) : ViewModel() {
    var uiState by mutableStateOf(NewSaleUiState())
    // region Sale Data
    fun getSale(){
        setCanModifyClient(false)
        baseViewModel.showLoader()
        viewModelScope.launch {
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)

            if (baseViewModel.isSessionValid()){
                setIdSale(uiState.sale.folio.toString())
                val r = getSalesByIdUseCase(uiState.idSale, uiState.internetUse)
                if (r.first != null){
                    setSaleData(r.first!!)
                    for (p in uiState.saleData.ventaProductos){
                        Log.i("Sales___", "${p}")
                    }
                    uiState.products.clear()
                    setProducts(r.first!!.ventaProductos)
                }
            }
            else{
                baseViewModel.dialogLogin.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    val editStatus = MutableStateFlow(false)
    fun editSale(onSuccesss: () -> Unit){
        setCanModifyClient(false)
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (baseViewModel.isSessionValid()){
                setInternetUse(Helpers.isInternetAvailable(cnx))
                uiState.saleData.ventaProductos = uiState.products
                uiState.saleData.ventaIdInterno = null
                val r = editSaleUseCase(uiState.saleData, uiState.idSale, uiState.internetUse)
                if (r.first != null){ setEditStatus(true) }
                else{
                    if (r.second != null){
                        MainActivity.mainDialogMsg.value = r.second!!
                        MainActivity.mainDialog.value = true
                    }
                }
            }
            else{ baseViewModel.dialogLogin.value = true }
            baseViewModel.hideLoader()
        }
    }
    fun deleteRow(data: SaleProductModel){
        uiState.products.remove(data)
        var newTotal = BigDecimal.ZERO

        for (p in uiState.products) {
            val precio = p.PrecioVenta ?: 0.0
            val cantidad = p.Cantidad ?: 0
            newTotal += (precio * cantidad.toDouble()).toBigDecimal()
            Log.i("Total_Product___", "$precio * $cantidad = $newTotal")
        }
        uiState.sale = uiState.sale.copy(total = newTotal.toDouble())
        uiState.saleData = uiState.saleData.copy(total = newTotal.toDouble())
    }
    fun addRow(data: ProductsResponseModel){
        var newTotal = BigDecimal(0.0)
        val p = if (uiState.canModifyClient) data.productoId ?: 0 else 0
        uiState.products.add(
            SaleProductModel(
                VentaProductoId = 0,
                VentaId = uiState.sale.ventaId,
                ProductoId = data.productoId,
                Cantidad = uiState.quantity.toInt(),
                PrecioVenta = uiState.price,
                Costo = data.costo,
                CantidadSolicitada = uiState.quantity.toInt(),
                VentaIdInterno = null,
                Venta = null,
                nombreProducto = data.descripcion!!
            )
        )
        for(p in uiState.products){
            newTotal += (p.PrecioVenta!! * p.Cantidad!!.toDouble()).toBigDecimal()
            Log.i("Total_Product___", "${p.PrecioVenta} * ${p.Cantidad} = ${newTotal}")
        }
        uiState.sale = uiState.sale.copy(total = newTotal.toDouble())
        uiState.saleData = uiState.saleData.copy(total = newTotal.toDouble())
    }
    fun getClients(){
        viewModelScope.launch {
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            baseViewModel.showLoader()
            val r = getClientsUseCase(uiState.internetUse)
            if (r.first != null){ setClients(r.first!!) }
            baseViewModel.hideLoader()
        }
    }
    fun filterClients(): ArrayList<ClientResponseModel> {
        setExpandSearchBar(true)
        setOpcions(
            if (uiState.client.text.isBlank()){
                setExpandSearchBar(false)
                arrayListOf()
            }
            else {
                setExpandSearchBar(true)
                ArrayList(uiState.clients.filter {
                    it.nombreCliente!!.contains(uiState.client.text, ignoreCase = true)
                })
            }
        )
        if (uiState.client.text.length > 17 ) setExpandSearchBar(false)
        return uiState.opcions
    }
    // endregion
    // region Dialog Products
    fun getFilter(): ArrayList<ProductsResponseModel> {
        val data = uiState.inventory ?: arrayListOf()
        val query = uiState.search.text.trim()
        setFilterInventory(
            if (query.length < 3) {
                setExpandSearchBarD(false)
                ArrayList(data)
            }
            else {
                if (query.length < 8) setExpandSearchBarD(true)
                else setExpandSearchBarD(false)
                ArrayList(data.filter {
                    it.descripcion?.contains(query, ignoreCase = true) == true
                })
            }
        )
        return uiState.filterInventory
    }
    fun getProducts(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            val r = getProductsUseCase(uiState.internetUse)
            if (r.first != null){
                setInventory(r.first!! as ArrayList<ProductsResponseModel>)
            }
            baseViewModel.hideLoader()
        }
    }

    fun getProductInventario(productId: Int){
        viewModelScope.launch {
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            baseViewModel.showLoader()
            val r = getInventarioProductoUseCase(productId, uiState.internetUse)
            if (r.first != null){
                setTotalInventory(r.first!!)
            }
            setExpandSearchBarD(false)
            setSerchProductId(productId)
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region New Sale

    fun createSale(onSuccesss: () -> Unit){
        baseViewModel.showLoader()
        viewModelScope.launch {
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            val userId = baseViewModel.getPerfilId()
            if (baseViewModel.isSessionValid()){
                var totalSale = 0.0
                val fecha = Helpers.getDateTime().replace(" ", "T")
                for (p in uiState.products){
                    totalSale += (p.PrecioVenta!! * p.Cantidad!!)
                    uiState.newProducts.add(
                        PostSaleProductModel(
                            cantidad = p.CantidadSolicitada,
                            productoId = p.ProductoId,
                            precioVenta = p.PrecioVenta,
                            costo = p.Costo,
                            ventaId = 0,
                        )
                    )
                }
                uiState.newSale.add(PostSalesModel(
                    clienteId = uiState.newClient!!.clienteId,
                    ventaId = 0,
                    esActivo = true,
                    fechaIngreso = fecha,
                    fechaVenta = fecha,
                    tipoPagoId = 1,
                    direccion = uiState.newClient!!.direccion ?: "null",
                    subtotal = 0.0,
                    iva = 0.0,
                    retencion = 0.0,
                    total = totalSale,
                    usuarioSesionId = userId,
                    ventaProductos = uiState.newProducts,
                    tipoConexionId = if (uiState.internetUse) 1 else 2
                ))
                val r = postSaleUseCase(uiState.newSale, uiState.internetUse)
                if (r.first != null){
                    MainActivity.mainDialogMsg.value = "Venta guardada"
                    setServerPostSale(true)
                    onSuccesss()
                }
            }
            else{ baseViewModel.dialogLogin.value = true }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Clean
    fun clearSales(){
        uiState.sale = SalesModel()
        uiState.newSale = arrayListOf()
        uiState.newClient = null
        uiState.newProducts = arrayListOf()
        uiState.products.clear()
    }
    fun clearDialog() {
        uiState.dialogProduct = false
        uiState.search = TextFieldValue("")
        uiState.opcions.clear()
        uiState.product = null
        uiState.expandenSearchBarD = false
        uiState.selectedProduct = null
        uiState.quantity = ""
        uiState.price = 0.0
    }
    fun clearClient(){
        uiState.client = TextFieldValue("")
        uiState.opcions.clear()
    }
    // endregion
    // region changue uiState
    fun setSale(data: SalesModel){ uiState = uiState.copy(sale = data) }
    fun setIdSale(data: String){ uiState = uiState.copy(idSale = data) }
    fun setSaleData(data: GetSalesByIdResponse){ uiState = uiState.copy(saleData = data) }
    fun setInternetUse(data: Boolean){ uiState = uiState.copy(internetUse = data) }
    fun setCanModifyClient(data: Boolean){ uiState = uiState.copy(canModifyClient = data) }
    fun setProducts(data: ArrayList<SaleProductModel>){ uiState = uiState.copy(products = data) }

    fun setExpandSearchBar(data: Boolean){ uiState = uiState.copy(expandenSearchBarS = data) }
    fun setClient(data: TextFieldValue){ uiState = uiState.copy(client = data) }
    fun setClients(data: List<ClientResponseModel>){ uiState = uiState.copy(clients = data) }
    fun setOpcions(data: ArrayList<ClientResponseModel>){ uiState = uiState.copy(opcions = data) }
    fun setEditStatus(data: Boolean){ uiState = uiState.copy(editStatus = data) }
    fun setDialogProduct(data: Boolean){ uiState = uiState.copy(dialogProduct = data) }
    fun setExpandSearchBarD(data: Boolean){ uiState = uiState.copy(expandenSearchBarD = data) }
    fun setSearch(data: TextFieldValue){ uiState = uiState.copy(search = data) }
    fun setInventory(data: ArrayList<ProductsResponseModel>){ uiState = uiState.copy(inventory = data) }
    fun setFilterInventory(data: ArrayList<ProductsResponseModel>){ uiState = uiState.copy(filterInventory = data) }

    fun setPrice(data: Double){ uiState = uiState.copy(price = data) }
    fun setQuantity(data: String){ uiState = uiState.copy(quantity = data) }
    fun setSerchProductId(data: Int){ uiState = uiState.copy(serchProductId = data) }
    fun setProduct(data: ProductEntity?){ uiState = uiState.copy(product = data) }
    fun setTotalInventory(data: ProductIdResponseModel){ uiState = uiState.copy(totalInventory = data) }
    fun setSelectedProduct(data: ProductsResponseModel?){ uiState = uiState.copy(selectedProduct = data) }

    fun setServerPostSale(data: Boolean){ uiState = uiState.copy(serverPostSale = data) }
    fun setNewSale(data: ArrayList<PostSalesModel>){ uiState = uiState.copy(newSale = data) }
    fun setNewClient(data: ClientResponseModel?){ uiState = uiState.copy(newClient = data) }
    fun setNewProducts(data: ArrayList<PostSaleProductModel>){ uiState = uiState.copy(newProducts = data) }
    // endregion
    init {
        clearSales()
        getProducts()
        getClients()
        Log.i("NewSaleViewModel___", "${uiState.sale}")
        Log.i("NewSaleViewModel___", "${uiState.newSale}")
    }
}
data class NewSaleUiState(
    var sale: SalesModel = SalesModel(),
    var idSale: String = "",
    var saleData: GetSalesByIdResponse = GetSalesByIdResponse(),
    val products: ArrayList<SaleProductModel> = arrayListOf(),


    val internetUse: Boolean = false,
    val canModifyClient: Boolean = true,

    val expandenSearchBarS: Boolean = true,
    var client: TextFieldValue = (TextFieldValue("")),
    val clients: List<ClientResponseModel> = listOf(),
    val opcions: ArrayList<ClientResponseModel> = arrayListOf(),
    val editStatus: Boolean = false,

    var dialogProduct: Boolean = (false),
    var expandenSearchBarD: Boolean = (false),
    var search: TextFieldValue = TextFieldValue(""),
    val inventory: ArrayList<ProductsResponseModel>? = arrayListOf(),
    val filterInventory: ArrayList<ProductsResponseModel> = arrayListOf(),

    var price: Double = 0.00,
    var quantity: String = "",
    val serchProductId: Int = 0,
    var product: ProductEntity? = null,
    val totalInventory: ProductIdResponseModel = (ProductIdResponseModel()),
    var selectedProduct: ProductsResponseModel? = null,

    val serverPostSale: Boolean = false,
    var newSale: ArrayList<PostSalesModel> = (arrayListOf()),
    var newClient: ClientResponseModel? = (null),
    var newProducts: ArrayList<PostSaleProductModel> = (arrayListOf())
)

