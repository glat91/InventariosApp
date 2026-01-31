package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.database.dao.ProductDao
import com.example.inventariosapp.database.entity.ProductEntity
import com.example.inventariosapp.domain.repository.client.GetClientsRepositoryImp
import com.example.inventariosapp.domain.repository.product.GetInventarioProductoRepositoryImp
import com.example.inventariosapp.domain.repository.product.GetProductsRepositoryImp
import com.example.inventariosapp.domain.repository.sales.EditSaleRepositoryImp
import com.example.inventariosapp.domain.repository.sales.GetSalesByIdRepositoryImp
import com.example.inventariosapp.domain.repository.sales.PostSaleRepositoryImp
import com.example.inventariosapp.model.client.ClientResponseModel
import com.example.inventariosapp.model.product.ProductIdResponseModel
import com.example.inventariosapp.model.product.ProductsResponseModel
import com.example.inventariosapp.model.sales.GetSalesByIdResponse
import com.example.inventariosapp.model.sales.PostSaleProductModel
import com.example.inventariosapp.model.sales.PostSalesModel
import com.example.inventariosapp.model.sales.SaleProductModel
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NewSaleViewModel @Inject constructor(
    private val getSalesByIdUseCase: GetSalesByIdRepositoryImp,
    private val editSaleUseCase: EditSaleRepositoryImp,
    private val postSaleUseCase: PostSaleRepositoryImp,
    private val getProductsUseCase: GetProductsRepositoryImp,
    private val getClientsUseCase: GetClientsRepositoryImp,
    private val productDao: ProductDao,
    private val getInventarioProductoUseCase: GetInventarioProductoRepositoryImp,
) : ViewModel() {
    val baseViewModel = BaseViewModel()
    val sale: MutableState<SalesModel> = mutableStateOf(SalesModel())

    val expandenSearchBarS = mutableStateOf(true)
    val client = mutableStateOf(TextFieldValue(""))
    val clients: MutableState<List<ClientResponseModel>> = mutableStateOf(listOf())
    val opcions: MutableState<ArrayList<ClientResponseModel>> = mutableStateOf(arrayListOf())

    // region Sale Data
    var idSale = ""
    val products: MutableState<ArrayList<SaleProductModel>> = mutableStateOf(arrayListOf())
    val saleData: MutableState<GetSalesByIdResponse> = mutableStateOf(GetSalesByIdResponse())
    fun getSale(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            idSale = sale.value.folio.toString()
            val r = getSalesByIdUseCase(idSale)
            if (r.first != null){
                saleData.value = r.first!!
                products.value = ArrayList(saleData.value.ventaProductos)
            }
            else{
                if (r.second != null){
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!

                } else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    val editStatus = MutableStateFlow(false)
    fun editSale(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            saleData.value.ventaProductos = products.value
            saleData.value.ventaIdInterno = null
            Log.i("Sale___", products.value.toString())
            val r = editSaleUseCase(saleData.value, idSale)
            if (r.first != null){
                editStatus.value = true
            }
            else{
                if (r.second != null){
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!

                } else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun deleteRow(data: SaleProductModel){
        products.value = ArrayList(products.value.filter { it != data })
        var newTotal = BigDecimal(0.0)
        for(p in products.value){
            newTotal += (p.PrecioVenta!! * p.Cantidad!!.toDouble()).toBigDecimal()
            Log.i("Total_Product___", "${p.PrecioVenta} * ${p.Cantidad} = ${newTotal}")

        }
        sale.value = sale.value.copy(
            total = newTotal.toDouble()
        )
        saleData.value = saleData.value.copy(
            total = newTotal.toDouble()
        )
    }
    fun addRow(data: ProductsResponseModel){
        var newTotal = BigDecimal(0.0)
        products.value.add(
            SaleProductModel(
                VentaProductoId = data.productoId,
                VentaId = sale.value.ventaId,
                ProductoId = data.productoId,
                Cantidad = quantity.value.toInt(),
                PrecioVenta = price.value,
                Costo = data.costo,
                CantidadSolicitada = quantity.value.toInt(),
                VentaIdInterno = null,
                Venta = null,
            )
        )
        for(p in products.value){
            newTotal += (p.PrecioVenta!! * p.Cantidad!!.toDouble()).toBigDecimal()
            Log.i("Total_Product___", "${p.PrecioVenta} * ${p.Cantidad} = ${newTotal}")

        }
        sale.value = sale.value.copy(total = newTotal.toDouble())
        saleData.value = saleData.value.copy(total = newTotal.toDouble())
    }
    fun getClients(){
        viewModelScope.launch {
            baseViewModel.showLoader()
            val r = getClientsUseCase(false)
            if (r.first != null){
                clients.value = r.first!!
            }
            else{
                if (r.second != null) {
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
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
    val inventory: MutableState<ArrayList<ProductsResponseModel>> = mutableStateOf(arrayListOf())
    val filterInventory: MutableState<ArrayList<ProductsResponseModel>> = mutableStateOf(arrayListOf())
    fun getFilter(): MutableState<ArrayList<ProductsResponseModel>> {
        filterInventory.value = if (search.value.text.isBlank()) {
            expandenSearchBarD.value = false
            inventory.value
        }
        else {
            expandenSearchBarD.value = true
            inventory.value.filter {
                it.descripcion!!.contains(search.value.text, ignoreCase = true)
            } as ArrayList<ProductsResponseModel>
        }
        return filterInventory
    }
    fun getProducts(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val r = getProductsUseCase(false)
            if (r.first != null){
                inventory.value = (r.first as ArrayList<ProductsResponseModel>?)!!
            }
            else{
                if (r.second != null) {
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }

    val price = mutableStateOf(0.0)
    val quantity = mutableStateOf("")
    val serchProductId = mutableStateOf(0)
    val product: MutableState<ProductEntity?> = mutableStateOf(null)
    val totalInventory: MutableState<ProductIdResponseModel> = mutableStateOf(ProductIdResponseModel())
    fun searchProductId(){
        viewModelScope.launch {
            product.value = productDao.getProductById(serchProductId.value)
            Log.i("ProductDao___", serchProductId.value.toString())
        }
    }
    val selectedProduct: MutableState<ProductsResponseModel?> = mutableStateOf(null)
    fun getProductInventario(productId: Int){
        viewModelScope.launch {
            baseViewModel.showLoader()
            val r = getInventarioProductoUseCase(productId)
            if (r.first != null){
                totalInventory.value = r.first!!
            }
            else{
                if (r.second != null) {
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
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
            val uuid = UUID.randomUUID().toString()
            var totalSale = 0.0
            for (p in products.value){
                totalSale += (p.PrecioVenta!! + p.Cantidad!!)
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
                ventaId = 0,
                esActivo = true,
                fechaIngreso = Helpers.getDateTime().replace(" ", "T").plus("Z"),
                fechaVenta = Helpers.getDateTime().replace(" ", "T").plus("Z"),
                tipoPagoId = 1,
                direccion = newClient.value!!.direccion ?: "null",
                subtotal = 0.0,
                iva = 0.0,
                retencion = 0,
                total = totalSale,
                usuarioSesionId = 1,
                ventaProductos = newProducts.value,
            ))
            val r = postSaleUseCase(newSale.value, false)
            if (r.first != null){
                MainActivity.mainDialogMsg.value = "Venta guardada"
                serverPostSale.value = true
            }
            else{
                if (r.second != null){
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!

                } else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
        }
        baseViewModel.hideLoader()
    }
    // endregion
    init {
        //getInventory()
        getProducts()
        getClients()
    }
}