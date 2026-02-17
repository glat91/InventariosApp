package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    val sale: MutableState<SalesModel> = mutableStateOf(SalesModel())
    val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
    val canModifyClient = mutableStateOf(true)

    val expandenSearchBarS = mutableStateOf(true)
    val client = mutableStateOf(TextFieldValue(""))
    val clients: MutableState<List<ClientResponseModel>> = mutableStateOf(listOf())
    val opcions: MutableState<ArrayList<ClientResponseModel>> = mutableStateOf(arrayListOf())

    // region Sale Data
    var idSale = ""
    val products = mutableStateListOf(SaleProductModel())
    val saleData: MutableState<GetSalesByIdResponse> = mutableStateOf(GetSalesByIdResponse())
    fun getSale(){
        canModifyClient.value = false
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (baseViewModel.isSessionValid()){
                idSale = sale.value.folio.toString()
                val r = getSalesByIdUseCase(idSale, internetUse.value)
                if (r.first != null){
                    saleData.value = r.first!!
                    for (p in saleData.value.ventaProductos){
                        Log.i("Sales___", "${p}")
                    }
                    products.clear()
                    products.addAll(saleData.value.ventaProductos)
                }
            }
            else{
                baseViewModel.dialogLogin.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    val editStatus = MutableStateFlow(false)
    fun editSale(){
        canModifyClient.value = false
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (baseViewModel.isSessionValid()){
                val internetUse = Helpers.isInternetAvailable(cnx)
                saleData.value.ventaProductos = java.util.ArrayList(products)
                saleData.value.ventaIdInterno = null
                Log.i("Sale___", products.toString())
                val r = editSaleUseCase(saleData.value, idSale, internetUse)
                if (r.first != null){ editStatus.value = true }
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
        products.remove(data)
        var newTotal = BigDecimal.ZERO

        for (p in products) {
            val precio = p.PrecioVenta ?: 0.0
            val cantidad = p.Cantidad ?: 0
            newTotal += (precio * cantidad.toDouble()).toBigDecimal()
            Log.i("Total_Product___", "$precio * $cantidad = $newTotal")
        }

        sale.value = sale.value.copy(total = newTotal.toDouble())
        saleData.value = saleData.value.copy(total = newTotal.toDouble())
    }
    fun addRow(data: ProductsResponseModel){
        var newTotal = BigDecimal(0.0)
        val p = if (canModifyClient.value) data.productoId ?: 0 else 0
        products.add(
            SaleProductModel(
                VentaProductoId = 0,
                VentaId = sale.value.ventaId,
                ProductoId = data.productoId,
                Cantidad = quantity.value.toInt(),
                PrecioVenta = price.value,
                Costo = data.costo,
                CantidadSolicitada = quantity.value.toInt(),
                VentaIdInterno = null,
                Venta = null,
                nombreProducto = data.descripcion!!
            )
        )
        for(p in products){
            newTotal += (p.PrecioVenta!! * p.Cantidad!!.toDouble()).toBigDecimal()
            Log.i("Total_Product___", "${p.PrecioVenta} * ${p.Cantidad} = ${newTotal}")
        }
        sale.value = sale.value.copy(total = newTotal.toDouble())
        saleData.value = saleData.value.copy(total = newTotal.toDouble())
    }
    fun getClients(){
        viewModelScope.launch {
            baseViewModel.showLoader()
            val r = getClientsUseCase(internetUse.value)
            if (r.first != null){
                clients.value = r.first!!
            }
            baseViewModel.hideLoader()

        }
    }
    fun filterClients(): MutableState<ArrayList<ClientResponseModel>> {
        expandenSearchBarS.value = true
        opcions.value = if (client.value.text.isBlank()){
            expandenSearchBarS.value = false
            arrayListOf()
        }
        else {
            expandenSearchBarS.value = true
            ArrayList(clients.value.filter {
                it.nombreCliente!!.contains(client.value.text, ignoreCase = true)
            })
        }
        if (client.value.text.length > 17 ) expandenSearchBarS.value = false
        return opcions
    }
    // endregion
    // region Dialog Products
    val dialogProduct = mutableStateOf(false)
    val expandenSearchBarD = mutableStateOf(false)
    var search = mutableStateOf(TextFieldValue(""))
    val inventory: MutableState<ArrayList<ProductsResponseModel>?> = mutableStateOf(arrayListOf())
    val filterInventory: MutableState<ArrayList<ProductsResponseModel>> = mutableStateOf(arrayListOf())
    fun getFilter(): MutableState<ArrayList<ProductsResponseModel>> {
        val data = inventory.value ?: arrayListOf()
        val query = search.value.text.trim()
        filterInventory.value =
            if (query.length < 3) {
                expandenSearchBarD.value = false
                ArrayList(data)
            }
            else {
                if (query.length < 8) expandenSearchBarD.value = true
                else expandenSearchBarD.value = false
                ArrayList(data.filter {
                    it.descripcion?.contains(query, ignoreCase = true) == true
                })
            }
        return filterInventory
    }
    fun getProducts(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val r = getProductsUseCase(internetUse.value)
            if (r.first != null){
                inventory.value = (r.first as ArrayList<ProductsResponseModel>?)!!
            }
            baseViewModel.hideLoader()
        }
    }

    val price = mutableStateOf(0.0)
    val quantity = mutableStateOf("")
    val serchProductId = mutableStateOf(0)
    val product: MutableState<ProductEntity?> = mutableStateOf(null)
    val totalInventory: MutableState<ProductIdResponseModel> = mutableStateOf(ProductIdResponseModel())
    val selectedProduct: MutableState<ProductsResponseModel?> = mutableStateOf(null)
    fun getProductInventario(productId: Int){
        viewModelScope.launch {
            baseViewModel.showLoader()
            val r = getInventarioProductoUseCase(productId, internetUse.value)
            if (r.first != null){
                totalInventory.value = r.first!!
            }
            expandenSearchBarD.value = false
            serchProductId.value = productId
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region New Sale
    val serverPostSale = MutableStateFlow(false)
    val newSale: MutableState<ArrayList<PostSalesModel>> = mutableStateOf(arrayListOf())
    val newClient: MutableState<ClientResponseModel?> = mutableStateOf(null)
    val newProducts: MutableState<ArrayList<PostSaleProductModel>> = mutableStateOf(arrayListOf())
    fun createSale(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val userId = baseViewModel.getPerfilId()
            if (baseViewModel.isSessionValid()){
                var totalSale = 0.0
                val fecha = Helpers.getDateTime().replace(" ", "T")
                for (p in products){
                    totalSale += (p.PrecioVenta!! * p.Cantidad!!)
                    newProducts.value.add(
                        PostSaleProductModel(
                            cantidad = p.CantidadSolicitada,
                            productoId = p.ProductoId,
                            precioVenta = p.PrecioVenta,
                            costo = p.Costo,
                            ventaId = 0,
                        )
                    )
                }
                newSale.value.add(PostSalesModel(
                    clienteId = newClient.value!!.clienteId,
                    nombreCliente = newClient.value!!.nombreCliente,
                    ventaId = 0,
                    esActivo = true,
                    fechaIngreso = fecha,
                    fechaVenta = fecha,
                    tipoPagoId = 1,
                    direccion = newClient.value!!.direccion ?: "null",
                    subtotal = 0.0,
                    iva = 0.0,
                    retencion = 0.0,
                    total = totalSale,
                    usuarioSesionId = userId,
                    ventaProductos = newProducts.value,
                    tipoConexionId = if (internetUse.value) 1 else 2
                ))
                val r = postSaleUseCase(newSale.value, internetUse.value)
                if (r.first != null){
                    MainActivity.mainDialogMsg.value = "Venta guardada"
                    serverPostSale.value = true
                }
            }
            else{
                baseViewModel.dialogLogin.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Clean
    fun clearSales(){
        sale.value = SalesModel()
        newSale.value = arrayListOf()
        newClient.value = null
        newProducts.value = arrayListOf()
        products.clear()
    }
    fun clearDialog() {
        dialogProduct.value = false
        search.value = TextFieldValue("")
        opcions.value.clear()
        product.value = null
        expandenSearchBarD.value = false
        selectedProduct.value = null
        quantity.value = ""
        price.value = 0.0
    }
    fun clearClient(){
        client.value = TextFieldValue("")
        opcions.value.clear()
    }
    // endregion
    init {
        clearSales()
        getProducts()
        getClients()
        Log.i("NewSaleViewModel___", "${sale.value}")
        Log.i("NewSaleViewModel___", "${newSale.value}")
    }
}

