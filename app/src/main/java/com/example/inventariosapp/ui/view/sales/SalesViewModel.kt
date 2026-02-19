package com.example.inventariosapp.ui.view.sales

import android.content.Context
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.use_case.sales.GetPendingSalesUseCase
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.util.Helpers
import com.example.inventariosapp.util.Helpers.Companion.deletePersistKey
import com.example.inventariosapp.util.Helpers.Companion.savePersistData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val getPendingSalesUseCase: GetPendingSalesUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context,
): ViewModel() {
    // region Date
    var selectedDate = mutableStateOf(LocalDate.now())
    val dialogChoice = mutableStateOf(false)
    var showDatePicker = mutableStateOf(false)
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDateInput(datePickerState: DatePickerState){
        val millis = datePickerState.selectedDateMillis

        if (millis != null) {
            selectedDate.value = Instant.ofEpochMilli(millis)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
        }
        if (dialogChoice.value) {
            MainActivity.endDate.value = selectedDate.value.toString()
        }
        else MainActivity.startDate.value = selectedDate.value.toString()
        getPendingSales()
    }
    // endregion
    // region Sales
    val searchSale = mutableStateOf(TextFieldValue(""))
    val sales: MutableState<ArrayList<SalesModel>> = mutableStateOf(arrayListOf())
    val salesFilter: MutableState<ArrayList<SalesModel>> = mutableStateOf(arrayListOf())
    fun getPendingSales(){
        baseViewModel.showLoader()
        viewModelScope.launch{
            val internetUse = MainActivity.internetBtn.value
            MainActivity.internetBtn.value = internetUse
            if (MainActivity.startDate.value.isBlank() && MainActivity.endDate.value.isBlank()) {
                MainActivity.startDate.value = Helpers.getYesterday()
                MainActivity.endDate.value = Helpers.getTomrrow()
            } else {
                if (MainActivity.startDate.value.isBlank()) { MainActivity.startDate.value = Helpers.getDate() }
                if (MainActivity.endDate.value.isBlank()) { MainActivity.endDate.value = Helpers.getTomrrow() }
            }
            try {
                val r = getPendingSalesUseCase("1",MainActivity.startDate.value, MainActivity.endDate.value, internetUse)
                if (r.first != null){ sales.value = r.first!! as ArrayList<SalesModel> }
            }
            catch (e: Exception){
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun getFilterSales(): MutableState<ArrayList<SalesModel>> {
        salesFilter.value = if (searchSale.value.text.isBlank()){
            sales.value
        } else ArrayList(
            sales.value.filter {
                it.nombreCliente!!.contains(searchSale.value.text, ignoreCase = true)
            }
        )
        return salesFilter
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

}