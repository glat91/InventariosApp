package com.example.inventariosapp.ui.view.user_payments

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.database.entity.PostSaleWithProducts
import com.example.inventariosapp.domain.use_case.payment.PostPaymentUseCase
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UserPaymentsViewModel @Inject constructor(
    private val postPaymentUseCase: PostPaymentUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
): ViewModel(){
    val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)

    var penndingPayments: MutableState<ArrayList<PostSaleWithProducts>> = mutableStateOf(arrayListOf())
}