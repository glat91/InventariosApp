package com.example.inventariosapp.ui.view.user_payments

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserPaymentsUiState(
    val penndingPayments: List<NewPayModel> = arrayListOf()
)
@HiltViewModel
class UserPaymentsViewModel @Inject constructor(
    private val postPaymentUseCase: PostPaymentUseCase,
    private val getPenndingPaymentUseCase: GetPenndingPaymentUseCase,
    val baseViewModel: BaseViewModel,
    private val newPayDao: NewPayDao,
    @ApplicationContext val cnx: Context
): ViewModel(){

    // region UiState
    private val _uiState = MutableStateFlow(UserPaymentsUiState())
    val uiState = _uiState.asStateFlow()
    // endregion

    // region State Management
    private fun updatePenndingPayments(penndingPayments: List<NewPayModel>) {
        _uiState.update { it.copy(penndingPayments = penndingPayments) }
    }
    // endregion

    fun setPayment() {
        val currentPayments = uiState.value.penndingPayments
        if (currentPayments.isNotEmpty()) {
            baseViewModel.showLoader()
            viewModelScope.launch {
                val userID = baseViewModel.getUsiarioId()
                val updatedPayments = currentPayments.map {
                    it.apply {
                        usuarioSesionId = userID
                        tipoConexionId = 2
                        origenId = 2
                    }
                }
                val internetUse = Helpers.isInternetAvailable(cnx)
                if (internetUse) {
                    val r = postPaymentUseCase(
                        internetUse = internetUse,
                        newPay = updatedPayments
                    )
                    if (r.isSuccess) {
                        newPayDao.deleteAll()
                        getPenndingPayments()
                        MainActivity.mainDialogMsg.value = "Pago realizado con exito"
                        MainActivity.mainDialog.value = true
                    }
                } else {
                    MainActivity.mainDialogMsg.value = "No hay conexion a internet"
                    MainActivity.mainDialog.value = true
                }
                baseViewModel.hideLoader()
            }
        } else {
            MainActivity.mainDialogMsg.value = "No tiene pagos pendientes por subir"
            MainActivity.mainDialog.value = true
        }
    }

    fun getPenndingPayments() {
        viewModelScope.launch {
            val payments = getPenndingPaymentUseCase()
            Log.i("Payments___", "$payments")
            updatePenndingPayments(payments)
        }
    }
}