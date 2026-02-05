package com.example.inventariosapp.ui.view.payment

import android.content.Context
import android.util.Log
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneric.domain.sales.GetPendingSalesRepositoryImp
import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.repository.client.GetClientsRepositoryImp
import com.example.inventariosapp.domain.repository.payment.DeletePaymentRepositoryImp
import com.example.inventariosapp.domain.repository.payment.GetPaymentRepositoryImp
import com.example.inventariosapp.domain.repository.payment.PostPaymentRepositoryImp
import com.example.inventariosapp.model.client.ClientResponseModel
import com.example.inventariosapp.model.payment.PayModel
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.util.Constants
import com.example.inventariosapp.util.Helpers
import com.example.inventariosapp.util.Helpers.Companion.readPersistData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val getClientsUseCase: GetClientsRepositoryImp,
    private val getPendingSalesUseCase: GetPendingSalesRepositoryImp,
    private val getPaymentUseCase: GetPaymentRepositoryImp,
    private val postPaymentUseCase: PostPaymentRepositoryImp,
    private val deletePaymentUseCase: DeletePaymentRepositoryImp,
) : ViewModel() {
    val baseViewModel = BaseViewModel()
    // region Date
    //val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    var selectedDate = mutableStateOf(LocalDate.now())
    var showDatePicker = mutableStateOf(false)

    val startDate = mutableStateOf("")
    val endDate = mutableStateOf("")
    val dialogChoice = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDateInput(datePickerState: DatePickerState){
        val millis = datePickerState.selectedDateMillis
        if (millis != null) {
            selectedDate.value = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        if (dialogChoice.value) endDate.value = selectedDate.value.toString()
        else startDate.value = selectedDate.value.toString()
    }
    // endregion
    // region Clients list
    val clients: MutableState<List<ClientResponseModel>> = mutableStateOf(listOf())
    fun getClients(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val r = getClientsUseCase(false)
            if (r.first != null){ clients.value = r.first!! }
            else {
                if (r.second != null){
                    val msg = r.second!!.MsgError!!.errors!!.first().errorMessage.toString()
                    MainActivity.mainDialogMsg.value = msg
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Sales
    val select: MutableState<SalesModel?> = mutableStateOf(null)
    val sales: MutableState<List<SalesModel>> = mutableStateOf(arrayListOf())
    fun getPendingSales(){
        baseViewModel.showLoader()
        if (startDate.value.isBlank() && endDate.value.isBlank()) {
            startDate.value = Helpers.getYesterday()
            endDate.value = Helpers.getTomrrow()
        } else {
            if (startDate.value.isBlank()) { startDate.value = Helpers.getDate() }
            if (endDate.value.isBlank()) { endDate.value = Helpers.getTomrrow() }
        }
        viewModelScope.launch {
            val r = getPendingSalesUseCase(startDate.value, endDate.value, false)
            if (r.first != null) {
                Log.i("Sales___", r.first!!.toString())
                sales.value = r.first!!
            }
            else{
                if (r.second != null){
                    val msg = r.second!!.MsgError!!.errors!!.first().errorMessage.toString()
                    MainActivity.mainDialogMsg.value = msg
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Dialog
    val payActualDate = Helpers.getDate()
    val payTotalPayment = mutableStateOf("")
    val payObservation = mutableStateOf("")
    val dialogDeposit = mutableStateOf(false)
    val showDeposit = mutableStateOf(false)
    val payments: MutableState<ArrayList<PayModel>> = mutableStateOf(arrayListOf())
    fun getPayment(ventaID: String){
        baseViewModel.showLoader()
        viewModelScope.launch {
            var r = getPaymentUseCase(ventaID, false)
            if (r.first != null){
                dialogDeposit.value = true
                payments.value = r.first!!
            }
            else{
                if (r.second != null){
                    val msg = r.second!!.MsgError!!.errors!!.first().errorMessage.toString()
                    MainActivity.mainDialogMsg.value = msg
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun setPayment(
        ventaId: Int,
        montoPago: Double,
        observaciones: String,
        cnx: Context,
    ){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val userID = cnx.readPersistData(Constants.USUARIO_ID, 0)
            Log.i("UserID___", userID.toString())
            val p = NewPayModel(
                ventaId = ventaId,
                montoPago = montoPago,
                fecha = Helpers.getDateTime().replace(" ", "T").plus("Z"),
                observaciones = observaciones,
                origenId = 1,
                tipoConexionId = 1,
                usuarioSesionId = userID
            )
            var r = postPaymentUseCase(newPay = listOf(p))
            if (r.isSuccess){
                MainActivity.mainDialogMsg.value = "Pago realizado con exito"
                MainActivity.mainDialog.value = true
                cleanDialog()
            }
            else{
                MainActivity.mainDialogMsg.value = "Error 1001100"
                MainActivity.mainDialog.value = true
                cleanDialog()
            }
            getPendingSales()
        }
    }
    fun deletePayment(pagoId: Int, deposit: PayModel){
        baseViewModel.showLoader()
        viewModelScope.launch {
            var r = deletePaymentUseCase(pagoId)
            if (r.isSuccess){
                dialogDeposit.value = false
                MainActivity.mainDialogMsg.value = "Pago borrado exitosamente"
                MainActivity.mainDialog.value = true
                payments.value.remove(deposit)
            }
            else{
                MainActivity.mainDialogMsg.value = "Error al borrar el pago"
                MainActivity.mainDialog.value = true
            }
            getPendingSales()
        }
    }
    // endregion
    // region clean
    fun cleanPayment(){
        payTotalPayment.value = ""
        payObservation.value = ""
    }
    fun cleanDialog(){
        dialogDeposit.value = false
        dialogChoice.value = false
        showDeposit.value = true
        showDatePicker.value = false
        selectedDate.value = LocalDate.now()
        cleanPayment()
    }
    // endregion
    init { getPendingSales() }
}