package com.example.inventariosapp.ui.view.user_sales

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
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
    private val postSaleUseCase: PostSaleUseCase,
    private val postSalesDao: PostSalesDao,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
): ViewModel() {
    var uiState by mutableStateOf(PenndingSalesUiState())

    private fun getPenndingSales(){
        viewModelScope.launch {
            val penndingSales = postSalesDao.getAllSales()
            setPenndingSales(ArrayList(penndingSales))
        }
    }
    fun updateSales(){
        if (uiState.penndingSales.size > 0){
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            baseViewModel.showLoader()
            viewModelScope.launch {
                val m = uiState.penndingSales.map {
                    it.sale.tipoConexionId = 2
                    it.toModel()
                }
                val r = postSaleUseCase(m, uiState.internetUse)
                if (r.first != null){
                    postSalesDao.deleteAll()
                    MainActivity.mainDialogMsg.value = "Venta guardada"
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

    init { getPenndingSales() }
    // region changue uiState
    fun setInternetUse(data: Boolean){ uiState = uiState.copy(internetUse = data) }
    fun setPenndingSales(data: ArrayList<PostSaleWithProducts>){ uiState = uiState.copy(penndingSales = data)}
    // endregion
}
data class PenndingSalesUiState(
    val internetUse: Boolean = false,
    var penndingSales: ArrayList<PostSaleWithProducts> = arrayListOf()
)