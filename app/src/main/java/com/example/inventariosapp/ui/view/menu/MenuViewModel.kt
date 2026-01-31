package com.example.inventariosapp.ui.view.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneric.domain.sales.GetPendingSalesRepositoryImp
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.repository.client.GetClientsRepositoryImp
import com.example.inventariosapp.domain.repository.payment.GetPaymentRepositoryImp
import com.example.inventariosapp.domain.repository.product.GetProductsRepositoryImp
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
    private val getProductsUseCase: GetProductsRepositoryImp,
    private val getClientsUseCase: GetClientsRepositoryImp,
    private val getPendingSalesUseCase: GetPendingSalesRepositoryImp,
    private val getPaymentUseCase: GetPaymentRepositoryImp,
    //private val getInventarioUseCase: GetInventarioUseCase,
    @ApplicationContext val cnx: Context
) : ViewModel() {
    val baseViewModel = BaseViewModel()
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
            }
            else{
                if (r.second != null) {
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            MainActivity.mainDialog.value = true
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
            }
            else{
                if (r.second != null) {
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
            }
            MainActivity.mainDialog.value = true
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
    fun updatePayment(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val total = 0
            val sales = getPendingSalesUseCase(Helpers.get6Months(), Helpers.getDate(),false)
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
                    else{
                        if (r.second != null){
                            val msg = r.second!!.MsgError!!.errors!!.first().errorMessage.toString()
                            MainActivity.mainDialogMsg.value = msg
                        }
                        else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                    }
                }
            }
            else{
                if (sales.second != null){
                    MainActivity.mainDialogMsg.value = sales.second!!.MsgError!!.errors!!.first().errorMessage!!

                } else{ MainActivity.mainDialogMsg.value = "Error 1001100" }

            }
            baseViewModel.hideLoader()
        }
    }
    /* TODO Borrar Cambio implementacion Inventario es igual que Products
    fun updateInventory(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val r = getInventarioUseCase(true)
            if (r.first != null){
                MainActivity.mainDialogMsg.value = "Update correcto"
                MainActivity.lastUpdateInventory.value = Helpers.getDateTime()
                saveSincroTime(
                    cnx,
                    Constants.SINCRO_INVENTORY,
                    Helpers.getDateTime()
                )
            }
            else{
                if (r.second != null){
                    MainActivity.mainDialogMsg.value = r.second!!.MsgError!!.errors!!.first().errorMessage!!

                } else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
            }
            MainActivity.mainDialog.value = true
            baseViewModel.hideLoader()
        }

    }
     */
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