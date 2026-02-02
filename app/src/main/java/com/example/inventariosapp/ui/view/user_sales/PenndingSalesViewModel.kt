package com.example.inventariosapp.ui.view.user_sales

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.database.dao.PostSalesDao
import com.example.inventariosapp.database.entity.PostSaleWithProducts
import com.example.inventariosapp.database.entity.toModel
import com.example.inventariosapp.domain.repository.sales.PostSaleRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PenndingSalesViewModel @Inject constructor(
    private val postSaleUseCase: PostSaleRepositoryImp,
    private val postSalesDao: PostSalesDao,
): ViewModel() {
    val baseViewModel = BaseViewModel()
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
                val m = penndingSales.value.map { it.toModel() }
                val r = postSaleUseCase(m, true)
                if (r.first != null){
                    MainActivity.mainDialogMsg.value = "Venta guardada"
                }
                else{
                    if (r.second != null){
                        MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!

                    } else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                    MainActivity.mainDialog.value = true
                }
            }
            MainActivity.mainDialog.value = true
            baseViewModel.hideLoader()
        }
        else{
            MainActivity.mainDialogMsg.value = "No tiene ventas pendientes por subir"
            MainActivity.mainDialog.value = true
        }
    }

    init {
        getPenndingSales()
    }
}