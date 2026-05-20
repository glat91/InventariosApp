package com.example.inventariosapp.ui.view.sales

import android.content.Context
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.use_case.sales.GetPendingSalesUseCase
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

// uiState data class encapsulating all mutable states
data class UiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val dialogChoice: Boolean = false,
    val showDatePicker: Boolean = false,
    val searchSale: TextFieldValue = TextFieldValue(""),
    val sales: ArrayList<SalesModel> = arrayListOf(),
    val salesFilter: ArrayList<SalesModel> = arrayListOf()
)
@HiltViewModel
class SalesViewModel @Inject constructor(
    private val getPendingSalesUseCase: GetPendingSalesUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context,
): ViewModel() {
    // region uiSate
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    // endregion
    // region update uiState
    fun updateSelectedDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun updateDialogChoice(choice: Boolean) {
        _uiState.update { it.copy(dialogChoice = choice) }
    }

    fun updateShowDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun updateSearchSale(textFieldValue: TextFieldValue) {
        _uiState.update { it.copy(searchSale = textFieldValue) }
    }

    fun updateSales(newSales: ArrayList<SalesModel>) {
        _uiState.update { it.copy(sales = newSales) }
    }

    fun updateSalesFilter(filtered: ArrayList<SalesModel>) {
        _uiState.update { it.copy(salesFilter = filtered) }
    }
    // endregion
    // region Date
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDateInput(datePickerState: DatePickerState){
        val millis = datePickerState.selectedDateMillis

        if (millis != null) {
            val newDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
            updateSelectedDate(newDate)
        }

        if (_uiState.value.dialogChoice){
            MainActivity.endDate.value = _uiState.value.selectedDate.toString()
        } else {
            MainActivity.startDate.value = _uiState.value.selectedDate.toString()
        }
        getPendingSales()
    }

    // endregion
    // region Sales
    fun getPendingSales(){
        baseViewModel.showLoader()
        viewModelScope.launch{
            if (MainActivity.startDate.value.isBlank() && MainActivity.endDate.value.isBlank()) {
                MainActivity.startDate.value = Helpers.getYesterday()
                MainActivity.endDate.value = Helpers.getTomrrow()
            } else {
                if (MainActivity.startDate.value.isBlank()) { MainActivity.startDate.value = Helpers.getDate() }
                if (MainActivity.endDate.value.isBlank()) { MainActivity.endDate.value = Helpers.getTomrrow() }
            }
            try {
                val r = getPendingSalesUseCase("1",MainActivity.startDate.value, MainActivity.endDate.value)
                if (r.first != null){ updateSales(r.first!! as ArrayList<SalesModel>) }
            } catch (e: Exception){
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
            }
            baseViewModel.hideLoader()
        }
    }

    fun getFilterSales(): ArrayList<SalesModel> {
        val filtered = if (_uiState.value.searchSale.text.isBlank()){
            uiState.value.sales
        } else ArrayList(
            _uiState.value.sales.filter {
                it.nombreCliente!!.contains(_uiState.value.searchSale.text, ignoreCase = true)
            }
        )
        updateSalesFilter(filtered)
        return _uiState.value.salesFilter
    }
    // endregion
}