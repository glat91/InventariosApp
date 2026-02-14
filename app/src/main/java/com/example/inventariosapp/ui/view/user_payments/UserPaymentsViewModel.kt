package com.example.inventariosapp.ui.view.user_payments

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.local.dao.NewPayDao
import com.example.inventariosapp.domain.use_case.payment.GetPenndingPaymentUseCase
import com.example.inventariosapp.domain.use_case.payment.PostPaymentUseCase
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPaymentsViewModel @Inject constructor(
    private val postPaymentUseCase: PostPaymentUseCase,
    private val getPenndingPaymentUseCase: GetPenndingPaymentUseCase,
    val baseViewModel: BaseViewModel,
    private val newPayDao: NewPayDao,
    @ApplicationContext val cnx: Context
): ViewModel(){
    var uiState by mutableStateOf(UserPaymentsUiState())
    fun setPayment(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            val userID = baseViewModel.getUsiarioId()
            uiState.penndingPayments.map {
                it.usuarioSesionId = userID
                it.tipoConexionId = 2
                it.origenId = userID
            }
            var r = postPaymentUseCase(internetUse = baseViewModel.internetBtn.value, newPay = uiState.penndingPayments)
            if (r.isSuccess){
                newPayDao.deleteAll()
                MainActivity.mainDialogMsg.value = "Pago realizado con exito"
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun getPenndingPayments() {
        viewModelScope.launch { setPenndingPayments(getPenndingPaymentUseCase()) }
    }
    init { getPenndingPayments() }
    // region changue uiState
    fun setInternetUse(data: Boolean){ uiState = uiState.copy(internetUse = data) }
    fun setPenndingPayments(data: List<NewPayModel>){ uiState = uiState.copy(penndingPayments = data) }
    //endregion
}
data class UserPaymentsUiState(
    val internetUse: Boolean = false,
    var penndingPayments: List<NewPayModel> = arrayListOf()
)