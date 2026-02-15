package com.example.inventariosapp.ui.view.sales

import android.content.Context
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var uiState by mutableStateOf(SalesUiState())
    // region Date
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDateInput(datePickerState: DatePickerState){
        val millis = datePickerState.selectedDateMillis

        if (millis != null) {
            setSelectedDate(
                Instant.ofEpochMilli(millis)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate()
            )
        }
        if (uiState.dialogChoice) {
            MainActivity.endDate.value = uiState.selectedDate.toString()
        }
        else MainActivity.startDate.value = uiState.selectedDate.toString()
        getPendingSales()
    }
    // endregion
    // region Sales
    fun getPendingSales(){
        baseViewModel.showLoader()
        viewModelScope.launch{
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            MainActivity.internetBtn.value = uiState.internetUse
            if (MainActivity.startDate.value.isBlank() && MainActivity.endDate.value.isBlank()) {
                MainActivity.startDate.value = Helpers.getYesterday()
                MainActivity.endDate.value = Helpers.getTomrrow()
            } else {
                if (MainActivity.startDate.value.isBlank()) { MainActivity.startDate.value = Helpers.getDate() }
                if (MainActivity.endDate.value.isBlank()) { MainActivity.endDate.value = Helpers.getTomrrow() }
            }
            try {
                val r = getPendingSalesUseCase(MainActivity.startDate.value, MainActivity.endDate.value, uiState.internetUse)
                if (r.first != null){
                    setSales(r.first!! as ArrayList<SalesModel>)
                }
            }
            catch (e: Exception){
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    fun getFilterSales(): ArrayList<SalesModel> {
        setSalesFilter(
            if (uiState.searchSale.text.isBlank()){
                uiState.sales
            } else ArrayList(
                uiState.sales.filter {
                    it.nombreCliente!!.contains(uiState.searchSale.text, ignoreCase = true)
                }
        ))
        return uiState.salesFilter
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
    // region changue uiState
    fun setInternetUse(data: Boolean){ uiState = uiState.copy(internetUse = data) }
    fun setSelectedDate(data: LocalDate){ uiState = uiState.copy(selectedDate = data) }
    fun setDialogChoice(data: Boolean){ uiState = uiState.copy(dialogChoice = data) }
    fun setShowDatePicker(data: Boolean){ uiState = uiState.copy(showDatePicker = data) }

    fun setSearchSale(data: TextFieldValue){ uiState = uiState.copy(searchSale = data) }
    fun setSales(data: ArrayList<SalesModel>){ uiState = uiState.copy(sales = data) }
    fun setSalesFilter(data: ArrayList<SalesModel>){ uiState = uiState.copy(salesFilter = data) }
    // endregion
}
data class SalesUiState(
    var internetUse: Boolean = false,
    var selectedDate: LocalDate = LocalDate.now(),
    var dialogChoice: Boolean = false,
    var showDatePicker: Boolean = false,

    var searchSale: TextFieldValue = TextFieldValue(""),
    val sales: ArrayList<SalesModel> = arrayListOf(),
    val salesFilter: ArrayList<SalesModel> = arrayListOf(),
)