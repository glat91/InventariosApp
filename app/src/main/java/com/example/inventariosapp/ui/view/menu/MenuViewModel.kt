package com.example.inventariosapp.ui.view.menu

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.use_case.client.GetClientsUseCase
import com.example.inventariosapp.domain.use_case.payment.GetPaymentUseCase
import com.example.inventariosapp.domain.use_case.product.GetProductsUseCase
import com.example.inventariosapp.domain.use_case.sales.GetPendingSalesUseCase
import com.example.inventariosapp.util.Constants
import com.example.inventariosapp.util.Helpers
import com.example.inventariosapp.util.Helpers.Companion.deletePersistKey
import com.example.inventariosapp.util.Helpers.Companion.readPersistData
import com.example.inventariosapp.util.Helpers.Companion.savePersistData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getPendingSalesUseCase: GetPendingSalesUseCase,
    private val getPaymentUseCase: GetPaymentUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
) : ViewModel() {
    val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
    // region Servicios
    fun updateClientsDb(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val r = getClientsUseCase(true)
            if (r.first != null){
                MainActivity.mainDialogMsg.value = "Update correcto"
                MainActivity.lastUpdateClient.value = Helpers.getDateTime()
                saveSincroTime(
                    cnx,
                    Constants.SINCRO_CLIENTS,
                    Helpers.getDateTime()
                )
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun updateProductsDb(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val r = getProductsUseCase(true)
            if (r.first != null){
                MainActivity.mainDialogMsg.value = "Update correcto"
                MainActivity.lastUpdateProducts.value = Helpers.getDateTime()
                saveSincroTime(
                    cnx,
                    Constants.SINCRO_PRODUCTS,
                    Helpers.getDateTime()
                )
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun updatePendingSales(){
        baseViewModel.showLoader()
        viewModelScope.launch{
            val r = getPendingSalesUseCase(Helpers.get6Months(), Helpers.getDate(),true)
            if (r.first != null){
                MainActivity.mainDialogMsg.value = "Update correcto"
                MainActivity.lastUpdateSells.value = Helpers.getDateTime()
                saveSincroTime(
                    cnx,
                    Constants.SINCRO_SALES,
                    Helpers.getDateTime()
                )
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun updatePayment(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val sales = getPendingSalesUseCase(Helpers.get6Months(), Helpers.getDate(),true)
            if (sales.first != null){
                for (s in sales.first!!){
                    var r = getPaymentUseCase(s.ventaId.toString(), true)
                    if (r.first != null){
                        MainActivity.mainDialogMsg.value = "Update correcto"
                        MainActivity.lastUpdatePayments.value = Helpers.getDateTime()
                        saveSincroTime(
                            cnx,
                            Constants.SINCRO_PAY,
                            Helpers.getDateTime()
                        )

                    }
                }
            }
            MainActivity.mainDialog.value = true
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Persist Data
    fun saveSincroTime(cnx: Context, key: String, data: String){
        viewModelScope.launch {
            cnx.savePersistData(key = key, data = data)
        }
    }
    fun clearSincroTime(cnx: Context, key: String){
        viewModelScope.launch {
            cnx.deletePersistKey(key)
        }
    }
    fun saveBoolean(cnx: Context, key: String, data: Boolean){
        viewModelScope.launch {
            cnx.savePersistData(key = key, data = data)
        }
    }
    // endregion
    init {
        viewModelScope.launch {
            MainActivity.lastUpdateClient.value = cnx.readPersistData(Constants.SINCRO_CLIENTS, "")
            MainActivity.lastUpdateProducts.value = cnx.readPersistData(Constants.SINCRO_PRODUCTS, "")
            MainActivity.lastUpdateSells.value = cnx.readPersistData(Constants.SINCRO_SALES, "")
            MainActivity.lastUpdatePayments.value = cnx.readPersistData(Constants.SINCRO_PAY, "")
            MainActivity.lastUpdateInventory.value = cnx.readPersistData(Constants.SINCRO_INVENTORY, "")
            MainActivity.internetBtn.value = cnx.readPersistData(Constants.INTERNET, true)
        }
    }
}