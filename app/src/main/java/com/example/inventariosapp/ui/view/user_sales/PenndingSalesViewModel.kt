package com.example.inventariosapp.ui.view.user_sales

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.model.product.ProductIdResponseModel
import com.example.inventariosapp.domain.repository.product.GetInventarioProductoRepositoryImp
import com.example.inventariosapp.local.dao.PostSalesDao
import com.example.inventariosapp.local.entity.PostSaleWithProducts
import com.example.inventariosapp.local.entity.toModel
import com.example.inventariosapp.domain.use_case.sales.PostSaleUseCase
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PenndingSalesUiState(
    var penndingSales: ArrayList<PostSaleWithProducts> = arrayListOf(),
    val selectedPenndigSale: PostSaleWithProducts? = null,
    val enableBtn: Boolean = true,
    val dialogProduct: Boolean = false,
    val totalInventory: ProductIdResponseModel = ProductIdResponseModel()
)
@HiltViewModel
class PenndingSalesViewModel @Inject constructor(
    private val getInventarioProductoUseCase: GetInventarioProductoRepositoryImp,
    private val postSaleUseCase: PostSaleUseCase,
    private val postSalesDao: PostSalesDao,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
): ViewModel() {
    // region UiState
    private val _uiState = MutableStateFlow(PenndingSalesUiState())
    val uiState = _uiState.asStateFlow()
    // endregion
    // region Update UiState
    fun updatePenndingSales(penndingSales: ArrayList<PostSaleWithProducts>) {
        _uiState.update { it.copy(penndingSales = penndingSales) }
    }
    fun updateTotalInventory(totalInventory: ProductIdResponseModel) {
        _uiState.update { it.copy(totalInventory = totalInventory) }
    }
    fun updateBtnStatus(boolean: Boolean) {
        _uiState.update { it.copy(enableBtn = boolean) }
    }
    fun updateSelectedPenndigSale(selectedPenndigSale: PostSaleWithProducts?) {
        _uiState.update { it.copy(selectedPenndigSale = selectedPenndigSale) }
    }
    fun updateDialogProduct(boolean: Boolean) {
        _uiState.update {it.copy(dialogProduct = boolean) }
    }
    //endregion
    // region Servicios
    private fun getPenndingSales(){
        viewModelScope.launch {
            val t = postSalesDao.getAllSales() as ArrayList<PostSaleWithProducts>
            Log.i("Sales___", "${t}")
            updatePenndingSales(t)
            for(a in uiState.value.penndingSales){
                for(b in a.productos){
                    Log.i("Product___", "${b.cantidad}")
                }
            }
        }
    }
    fun updateSales(){
        if (!_uiState.value.enableBtn) return
        updateBtnStatus(false)
        val randomLong = (1L..9L).random()
        Thread.sleep(randomLong)
        if (uiState.value.penndingSales.size > 0){
            baseViewModel.showLoader()
            viewModelScope.launch {
                val userId = baseViewModel.getUsiarioId()
                if (userId != 0){
                    val userSales = uiState.value.penndingSales.map {
                        it.sale.tipoConexionId = 2
                        it.sale.usuarioSesionId = userId
                        it.sale.origenId = 2
                        it.toModel()
                    }
                    val internetUse = Helpers.isInternetAvailable(cnx)
                    if (internetUse){
                        val r = postSaleUseCase(userSales, internetUse)
                        if (r.first != null){
                            postSalesDao.deleteAllProducts()
                            postSalesDao.deleteAllSales()
                            getPenndingSales()
                            MainActivity.mainDialogMsg.value = "Ventas guardadas"
                            MainActivity.mainDialog.value = true
                        }
                        else {
                            MainActivity.mainDialogMsg.value = "Error al procesar la venta"
                            MainActivity.mainDialog.value = true
                        }
                    }
                    else{
                        MainActivity.mainDialogMsg.value = "No hay conexion a internet"
                        MainActivity.mainDialog.value = true
                    }
                }
                else{
                    baseViewModel.dialogLogin.value = true
                }
                updateBtnStatus(true)
                baseViewModel.hideLoader()
            }

        }
        else{
            MainActivity.mainDialogMsg.value = "No tiene ventas pendientes por subir"
            MainActivity.mainDialog.value = true
        }

    }
    // endregion
    fun deleteSale(id: Int) {
        val newList = ArrayList(uiState.value.penndingSales)
        newList.removeAll { it.sale.ventaId == id }
        updatePenndingSales(newList)
    }

    suspend fun getProductInventario(productId: Int){
        val internetUse = Helpers.isInternetAvailable(cnx)
        val r = getInventarioProductoUseCase(productId, internetUse)
        if (r.first != null){ updateTotalInventory(r.first!!) }
    }

    init { getPenndingSales() }
}
