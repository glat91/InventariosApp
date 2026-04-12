package com.example.inventariosapp.ui.view.user_sales

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.domain.model.sales.PostSalesModel
import com.example.inventariosapp.domain.repository.product.GetInventarioProductoRepositoryImp
import com.example.inventariosapp.local.dao.PostSalesDao
import com.example.inventariosapp.local.entity.PostSaleWithProducts
import com.example.inventariosapp.local.entity.toModel
import com.example.inventariosapp.domain.use_case.sales.PostSaleUseCase
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PenndingSalesViewModel @Inject constructor(
    private val getInventarioProductoUseCase: GetInventarioProductoRepositoryImp,
    private val postSaleUseCase: PostSaleUseCase,
    private val postSalesDao: PostSalesDao,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
): ViewModel() {
    var penndingSales: MutableState<ArrayList<PostSaleWithProducts>> = mutableStateOf(arrayListOf())
    val inventory: MutableState<ArrayList<ProductsResponseModel>?> = mutableStateOf(arrayListOf())
    val notInInventory = mutableListOf<PostSaleWithProducts>()
    val totalInventory: MutableState<ProductIdResponseModel> = mutableStateOf(ProductIdResponseModel())
    private fun getPenndingSales(){
        viewModelScope.launch {
            penndingSales.value = ArrayList(postSalesDao.getAllSales()) }
    }
    fun updateSales(){
        if (penndingSales.value.size > 0){
            baseViewModel.showLoader()
            viewModelScope.launch {
                val m = penndingSales.value.map {
                    it.sale.tipoConexionId = 2
                    it.toModel()
                }
                val internetUse = Helpers.isInternetAvailable(cnx)
                if (internetUse){
                    val r = postSaleUseCase(m, internetUse)
                    if (r.first != null){
                        postSalesDao.deleteAllProducts()
                        postSalesDao.deleteAllSales()
                        getPenndingSales()
                        MainActivity.mainDialogMsg.value = "Ventas guardadas"
                        MainActivity.mainDialog.value = true
                    }
                }
                else{
                    MainActivity.mainDialogMsg.value = "No hay conexion a internet"
                    MainActivity.mainDialog.value = true
                }
            }
            baseViewModel.hideLoader()
        }
        else{
            MainActivity.mainDialogMsg.value = "No tiene ventas pendientes por subir"
            MainActivity.mainDialog.value = true
        }
    }
    fun deleteSale(id: Int) {
        val newList = ArrayList(penndingSales.value)
        newList.removeAll { it.sale.ventaId == id }
        penndingSales.value = newList
    }

    suspend fun getProductInventario(productId: Int){
        val internetUse = Helpers.isInternetAvailable(cnx)
        val r = getInventarioProductoUseCase(productId, internetUse)
        if (r.first != null){
            totalInventory.value = r.first!!
        }
    }

    init { getPenndingSales() }
}
