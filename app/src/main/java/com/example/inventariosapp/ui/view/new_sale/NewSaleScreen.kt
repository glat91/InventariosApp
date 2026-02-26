package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.ui.dialog.AddProductDialogCmp
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.view.login.LoginViewModel
import com.example.inventariosapp.util.Helpers

@Composable
fun NewSaleScreen(navController: NavHostController) {
    val viewModel: NewSaleViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val editStatus = viewModel.editStatus.collectAsState()
    val postSale = viewModel.serverPostSale.collectAsState()
    val cnx = LocalContext.current

    LaunchedEffect(viewModel.expandenSearchBarS.value) {
        if (viewModel.newClient.value != null){
            Log.i("Expanded___2", viewModel.expandenSearchBarS.value.toString())
            viewModel.expandenSearchBarS.value = false
            viewModel.expandenSearchBarD.value = false
            Log.i("Expanded___2", viewModel.expandenSearchBarS.value.toString())
        }
    }
    // region Previous Data
    LaunchedEffect(true){
        try{
            viewModel.sale.value = navController.previousBackStackEntry?.savedStateHandle?.get<SalesModel>("sale")!!
            viewModel.getSale()
        }
        catch (e: Exception){ }
        viewModel.client.value = TextFieldValue(viewModel.sale.value.nombreCliente ?: "")
    }
    // endregion
    LaunchedEffect(editStatus.value){
        if (editStatus.value){
            navController.navigate(route = Destinations.SalesScreen.ruta){
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(postSale.value) {
        if (postSale.value){
            navController.navigate(route = Destinations.SalesScreen.ruta){
                launchSingleTop = true
                popUpTo(Destinations.NewSaleScreen.ruta){ inclusive = true }
            }
        }
    }
    // region Composable
    NewSaleView(
        clientName = viewModel.client,
        sale = viewModel.sale,
        salesData = viewModel.products,
        expandedSearchBar = viewModel.expandenSearchBarS,
        opcions = viewModel.filterClients(),
        canModify = viewModel.canModifyClient,
        onClickOpcion = {
            Log.i("Modify___", viewModel.canModifyClient.toString())
            if (viewModel.canModifyClient.value) {
                viewModel.newClient.value = it
                viewModel.client.value = TextFieldValue(it.nombreCliente.toString())
                viewModel.sale.value.nombreCliente = it.nombreCliente.toString()
                //viewModel.expandenSearchBarS.value = false
                Log.i("Expanded___", viewModel.expandenSearchBarS.value.toString())
            }
        },
        onClickDelete = { viewModel.deleteRow(it) },
        onClickProduct = { viewModel.dialogProduct.value = true },
        onClickSave = {
            if (viewModel.sale.value.ventaId == null) {
                if (viewModel.products.isNotEmpty() && viewModel.newClient.value != null) {
                    viewModel.createSale()
                }
            }
            else {
                if (viewModel.products.isNotEmpty()){ viewModel.editSale() }
            }
        },
        onClickBack = {
            navController.currentBackStackEntry?.savedStateHandle?.remove<SalesModel>("sale")
            navController.popBackStack()
        },
        onClickMenu = { viewModel.baseViewModel.openMenu() }
    )
    // endregion
    // region Dialog
    if (viewModel.dialogProduct.value){
        AddProductDialogCmp(
            state = viewModel.search,
            opcions = viewModel.getFilter().value,
            expanded = viewModel.expandenSearchBarD,
            product = viewModel.selectedProduct,
            quantity = viewModel.quantity,
            onDismiss = { viewModel.clearDialog() },
            onChangeText = { viewModel.search.value = it },
            onClickOpcion = {
                Log.i("Opcion___", it.toString())
                viewModel.selectedProduct.value = it
                //viewModel.expandenSearchBarD.value = false
                viewModel.getProductInventario(it.productoId!!)
            },
            onClickPrice = { viewModel.price.value = it },
            inventario = viewModel.totalInventory.value,
            onClickCancel = { viewModel.clearDialog() },
            onClickAccept = {
                viewModel.addRow(it)
                viewModel.clearDialog()
            }
        )
    }
    // endregion
    // region Dialog Login
    if (viewModel.baseViewModel.dialogLogin.value){
        LoginDialogCmp(
            user = lviewModel.user,
            password = lviewModel.password,
            rememberUser = remember { mutableStateOf(false) },
            onClickEnter = {
                if (!lviewModel.rememberUser.value) lviewModel.clearUser()
                else lviewModel.saveUserLogin()
                val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
                lviewModel.validateUserLogin(MainActivity.internetBtn.value)
            },
            onClickRememberPassword = {
                lviewModel.rememberUser.value = !lviewModel.rememberUser.value
            }
        )
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
}