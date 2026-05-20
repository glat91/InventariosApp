package com.example.inventariosapp.ui.view.sales

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(navController: NavHostController) {
    val viewModel: SalesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    @OptIn(ExperimentalMaterial3Api::class)
    var datePickerState1 = rememberDatePickerState()
    var datePickerState2 = rememberDatePickerState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getPendingSales()
        }
    }

    SalesView(
        search = viewModel.uiState.collectAsState().value.searchSale,
        dateEnd = MainActivity.endDate.value,
        dateStart = MainActivity.startDate.value,
        onSearchChangue = { viewModel.updateSearchSale(it) },
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickDate = { viewModel.updateShowDatePicker(true) },
        onclickRow = {
            if (MainActivity.internetBtn.value){
                navController.currentBackStackEntry?.savedStateHandle?.set("sale", it)
                navController.navigate(route = Destinations.NewSaleScreen.ruta){
                    launchSingleTop = true
                }
            }
            else{
                MainActivity.mainDialogMsg.value = "Modo offline activado, no es posible editar ventas"
                MainActivity.mainDialog.value = true
            }
        },
        onClickAdd = {
            navController.currentBackStackEntry?.savedStateHandle?.remove<SalesModel>("sale")
            navController.navigate(route = Destinations.NewSaleScreen.ruta){
                launchSingleTop = true
            }
        },
        data = viewModel.getFilterSales(),
        changueDialogChoice = { viewModel.updateDialogChoice(it) }
    )
    // region Dialog Date
    if (uiState.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.updateShowDatePicker(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateDateInput(
                            if (uiState.dialogChoice) datePickerState2 else datePickerState1
                        )
                        viewModel.updateShowDatePicker(false)
                    }) {
                    TextCmp("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.updateShowDatePicker(false) }) {
                    TextCmp("Cancelar")
                }
            }
        ) {
            DatePicker(state = if (uiState.dialogChoice) datePickerState2 else datePickerState1)
        }
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
    BackHandler(false) { }
}