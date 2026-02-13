package com.example.inventariosapp.ui.view.user_sales

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
    @ApplicationContext val cnx: Context,
    ): ViewModel() {
    val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)

    var penndingSales: MutableState<ArrayList<PostSaleWithProducts>> = mutableStateOf(arrayListOf())

    private fun getPenndingSales(){
        viewModelScope.launch {
            penndingSales.value = ArrayList(postSalesDao.getAllSales())
        }
    }
    fun updateSales(){
        if (penndingSales.value.size > 0){
            baseViewModel.showLoader()
            viewModelScope.launch {
                val m = penndingSales.value.map {
                    it.sale.tipoConexionId = 2
                    it.toModel()
                }
                val r = postSaleUseCase(m, internetUse.value)
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
}
data class PenndingSalesUiState(
    val internetUse: Boolean = false,
    var penndingSales: ArrayList<PostSaleWithProducts> = arrayListOf()
)