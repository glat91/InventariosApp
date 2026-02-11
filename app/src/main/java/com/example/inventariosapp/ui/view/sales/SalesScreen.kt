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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(navController: NavHostController) {
    val viewModel: SalesViewModel = hiltViewModel()
    val internetUse by viewModel.baseViewModel.internetUses.collectAsState()
    val cnx = LocalContext.current

    @OptIn(ExperimentalMaterial3Api::class)
    var datePickerState = rememberDatePickerState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getPendingSales()
        }
    }
    LaunchedEffect(internetUse) {
        if (!internetUse){
            MainActivity.mainDialogMsg.value = "Sin internet, modo Offline"
            MainActivity.internetBtn.value = false
            viewModel.saveBoolean(
                cnx,
                Constants.INTERNET,
                MainActivity.internetBtn.value
            )
            MainActivity.mainDialog.value = true
        }
        else{
            MainActivity.internetBtn.value = true
            viewModel.saveBoolean(
                cnx,
                Constants.INTERNET,
                MainActivity.internetBtn.value
            )
        }
    }

    SalesView(
        search = viewModel.searchSale,
        dateEnd = viewModel.endDate.value,
        dateStart = viewModel.startDate.value,
        dialogChoice = viewModel.dialogChoice,
        onSearchChangue = { viewModel.searchSale.value = it },
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickDate = { viewModel.showDatePicker.value = true },
        onclickRow = {
            navController.currentBackStackEntry?.savedStateHandle?.set("sale", it)
            navController.navigate(route = Destinations.NewSaleScreen.ruta){
                launchSingleTop = true
            }
        },
        onClickAdd = {
            navController.currentBackStackEntry?.savedStateHandle?.remove<SalesModel>("sale")
            navController.navigate(route = Destinations.NewSaleScreen.ruta){
                launchSingleTop = true
            }
        },
        data = viewModel.getFilterSales()
    )
    // region Dialog Date
    if (viewModel.showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { viewModel.showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateDateInput(datePickerState)
                        viewModel.showDatePicker.value = false
                    }) {
                    TextCmp("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDatePicker.value = false }) {
                    TextCmp("Cancelar")
                }
            }
        ) { DatePicker(state = datePickerState) }
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
    BackHandler(false) { }
}