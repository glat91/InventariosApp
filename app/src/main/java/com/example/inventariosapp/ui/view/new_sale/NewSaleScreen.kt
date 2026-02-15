package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun NewSaleScreen(navController: NavHostController) {
    val viewModel: NewSaleViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val editStatus = viewModel.editStatus.collectAsState()
    // region Previous Data
    LaunchedEffect(true){
        try{
            viewModel.uiState.sale = navController.previousBackStackEntry?.savedStateHandle?.get<SalesModel>("sale")!!
            viewModel.getSale()
        }
        catch (e: Exception){ }
        viewModel.uiState.client = TextFieldValue(viewModel.uiState.sale.nombreCliente ?: "")
    }
    // endregion
    // region Composable
    NewSaleView(
        clientName = viewModel.uiState.client,
        sale = viewModel.uiState.sale,
        salesData = viewModel.uiState.products,
        expandedSearchBar = viewModel.uiState.expandenSearchBarS,
        opcions = viewModel.filterClients(),
        canModify = viewModel.uiState.canModifyClient,
        onTextChangue = { viewModel.setClient(it)},
        onChangueExpandValue = { viewModel.setExpandSearchBar(it) },
        onClickOpcion = {
            Log.i("Modify___", viewModel.uiState.canModifyClient.toString())
            if (viewModel.uiState.canModifyClient) {
                viewModel.uiState.newClient = it
                viewModel.uiState.client = TextFieldValue(it.nombreCliente.toString())
                viewModel.uiState.sale.nombreCliente = it.nombreCliente.toString()
                viewModel.setExpandSearchBar(false)
            }
        },
        onClickDelete = { viewModel.deleteRow(it) },
        onClickProduct = { viewModel.uiState.dialogProduct = true },
        onClickSave = {
            if (viewModel.uiState.sale.ventaId == null) {
                if (viewModel.uiState.products.isNotEmpty() && viewModel.uiState.newClient != null) {
                    viewModel.createSale(onSuccesss = {
                        navController.navigate(route = Destinations.SalesScreen.ruta){
                            launchSingleTop = true
                            popUpTo(Destinations.NewSaleScreen.ruta){ inclusive = true }
                        }
                    })
                }
            }
            else {
                if (viewModel.uiState.products.isNotEmpty()){
                    viewModel.editSale(onSuccesss = {
                        navController.navigate(route = Destinations.SalesScreen.ruta){
                            launchSingleTop = true
                        }
                    })
                }
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
    if (viewModel.uiState.dialogProduct){
        AddProductDialogCmp(
            state = viewModel.uiState.search,
            opcions = viewModel.getFilter(),
            expanded = viewModel.uiState.expandenSearchBarD,
            product = viewModel.uiState.selectedProduct,
            quantity = viewModel.uiState.quantity,
            onDismiss = { viewModel.clearDialog() },
            onChangeText = { viewModel.setSearch(it) },
            onClickOpcion = {
                Log.i("Opcion___", it.toString())
                viewModel.setSelectedProduct(it)
                viewModel.setExpandSearchBarD(false)
                viewModel.getProductInventario(it.productoId!!)
            },
            onClickPrice = { viewModel.setPrice(it) },
            onChangueQuienatity = {
                // Permitir borrar
                if (it.isEmpty()) {
                    viewModel.setQuantity("")
                    return@AddProductDialogCmp
                }

                // Solo números
                if (!it.all { it.isDigit() }) return@AddProductDialogCmp

                val sanitized = when {
                    it == "0" -> "0"
                    it.startsWith("0") -> it.dropWhile { it == '0' }
                    else -> it
                }

                // Validar contra inventario
                val value = sanitized.toIntOrNull() ?: return@AddProductDialogCmp


                if (value <= (viewModel.uiState.totalInventory.inventario ?: 10000)) {
                    viewModel.setQuantity(sanitized)
                }
            },
            inventario = viewModel.uiState.totalInventory,
            onChangueExpandValue = { viewModel.setExpandSearchBarD(it) },
            onClickCancel = { viewModel.clearDialog() },
            onClickAccept = {
                viewModel.addRow(it)
                viewModel.clearDialog()
            },
            onChangueState = { }
        )
    }
    // endregion
    // region Dialog Login
    if (viewModel.baseViewModel.dialogLogin.value){
        LoginDialogCmp(
            user = lviewModel.uiState.user,
            password = lviewModel.uiState.password,
            rememberUser = (false),
            onChanguerUser = { lviewModel.onUserChange(it) },
            onChanguerPassword = { lviewModel.onPasswordChange(it) },
            onClickEnter = {
                if (!lviewModel.uiState.rememberUser) lviewModel.clearUser()
                else lviewModel.saveUserLogin()
                lviewModel.validateUserLogin(MainActivity.internetBtn.value)
            },
            onClickRememberPassword = {
                lviewModel.uiState.rememberUser = !lviewModel.uiState.rememberUser
            }
        )
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
}