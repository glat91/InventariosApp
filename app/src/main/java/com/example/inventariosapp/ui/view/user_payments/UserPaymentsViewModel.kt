package com.example.inventariosapp.ui.view.user_payments

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.database.dao.NewPayDao
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
    val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
    var penndingPayments: MutableState<List<NewPayModel>> = mutableStateOf(arrayListOf())

    fun setPayment(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val userID = baseViewModel.getUsiarioId()
            penndingPayments.value.map { 
                it.usuarioSesionId = userID
                it.tipoConexionId = 2
                it.origenId = userID
            }
            var r = postPaymentUseCase(internetUse = baseViewModel.internetBtn.value, newPay = penndingPayments.value)
            if (r.isSuccess){
                newPayDao.deleteAll()
                MainActivity.mainDialogMsg.value = "Pago realizado con exito"
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }

    fun getPenndingPayments() {
        viewModelScope.launch {
            penndingPayments.value = getPenndingPaymentUseCase()
        }
    }

    init {
        getPenndingPayments()
    }
}