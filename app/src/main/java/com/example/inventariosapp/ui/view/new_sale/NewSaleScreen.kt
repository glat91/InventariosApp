package com.example.inventariosapp.ui.view.new_sale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val addProductViewModel: AddProductDialogViewModel = hiltViewModel()
    val saleUiState by viewModel.uiState.collectAsState()
    val cnx = LocalContext.current
    val uiState by lviewModel.uiState.collectAsState()
    val addproductUiState by addProductViewModel.uiState.collectAsState()

    LaunchedEffect(saleUiState.expandenSearchBarS) {
        if (saleUiState.newClient != null) {
            viewModel.updateExpandenSearchBarS(false)
            viewModel.updateExpandenSearchBarD(false)
        }
    }
    // region Previous Data
    LaunchedEffect(true) {
        try {
            val sale = navController.previousBackStackEntry?.savedStateHandle?.get<SalesModel>("sale")!!
            viewModel.updateSale(sale)
            viewModel.getSale()
        } catch (e: Exception) { }
        viewModel.updateClient(TextFieldValue(saleUiState.sale.nombreCliente ?: ""))
    }
    // endregion
    LaunchedEffect(saleUiState.editStatus) {
        if (saleUiState.editStatus) {
            navController.navigate(route = Destinations.SalesScreen.ruta) {
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(saleUiState.serverPostSale) {
        if (saleUiState.serverPostSale) {
            navController.navigate(route = Destinations.SalesScreen.ruta) {
                launchSingleTop = true
                popUpTo(Destinations.NewSaleScreen.ruta) { inclusive = true }
            }
        }
    }
    // region Composable
    NewSaleView(
        clientName = saleUiState.client,
        sale = saleUiState.sale,
        salesData = viewModel.products,
        expandedSearchBar = saleUiState.expandenSearchBarS,
        opcions = viewModel.filterClients(),
        canModify = saleUiState.canModifyClient,
        onChangueSearch = { viewModel.updateClient(it) },
        onDissmissSearchBar = { viewModel.updateExpandenSearchBarD(false) },
        onClickOpcion = {
            if (saleUiState.canModifyClient) {
                viewModel.updateNewClient(it)
                viewModel.updateClient(TextFieldValue(it.nombreCliente.toString()))
                viewModel.updateSale(saleUiState.sale.copy(nombreCliente = it.nombreCliente.toString()))
                viewModel.updateExpandenSearchBarS(false)
            }
        },
        onClickDelete = { viewModel.deleteRow(it) },
        onClickProduct = {
            viewModel.updateDialogProduct(true)
        },
        onClickSave = {
            if (saleUiState.sale.ventaId == null) {
                if (viewModel.products.isNotEmpty() && saleUiState.newClient != null) {
                    viewModel.createSale()
                }
                else {
                    if (viewModel.products.isNotEmpty()){
                        MainActivity.mainDialogMsg.value = "No hay productos para guardar"
                        MainActivity.mainDialog.value = true
                    }
                    if (saleUiState.newClient != null){
                        MainActivity.mainDialogMsg.value = "No hay cliente para guardar"
                        MainActivity.mainDialog.value = true
                    }

                }
            } else {
                if (MainActivity.internetBtn.value) {
                    if (viewModel.products.isNotEmpty()) { viewModel.editSale() }
                } else {
                    MainActivity.mainDialogMsg.value = "Modo offline no activado"
                    MainActivity.mainDialog.value = true
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
    if (saleUiState.dialogProduct) {
        val loadComents: MutableState<String> = remember {
            mutableStateOf(
                if (saleUiState.canModifyClient) saleUiState.comentarios
                else saleUiState.selectedProduct?.comentarios ?: ""
            )
        }
        AddProductDialogCmp(
            uiState = addproductUiState,
            search = saleUiState.search,
            opcions = saleUiState.filterInventory,
            expanded = saleUiState.expandenSearchBarD,
            onlineProduct = saleUiState.selectedProduct,
            offlineProduct = saleUiState.inventoryOffline,
            onDismiss = {
                viewModel.clearDialog()
                addProductViewModel.resetData()
            },
            onChangeText = {
                viewModel.getFilter(it)
                           },
            onClickOpcion = {
                viewModel.updateSelectedProduct(it)
                viewModel.updateExpandenSearchBarD(false)
                viewModel.getProductInventario(it.productoId!!)
                addProductViewModel.resetData()
                viewModel.updatePrice(0.0)
            },
            onClickPrice = { viewModel.updatePrice(it) },
            inventario = saleUiState.totalInventory,
            onClickCancel = {
                viewModel.clearDialog()
                addProductViewModel.resetData()
            },
            onClickAccept = {
                viewModel.addRow(it, loadComents.value, addproductUiState)
                viewModel.clearDialog()
                addProductViewModel.resetData()
            },
            onUpdateState = { addProductViewModel.updateState(it) },
        )
    }
    // endregion
    // region Dialog Login
    if (viewModel.baseViewModel.dialogLogin.value) {
        LoginDialogCmp(
            uiState = uiState,
            onClickEnter = {
                val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
                if (!uiState.rememberUser) lviewModel.clearUser()
                else lviewModel.saveUserLogin()
                lviewModel.validateUserLogin(internetUse)
            },
            onUserChange = { lviewModel.updateUser(it) },
            onPasswordChange = { lviewModel.updatePassword(it) },
            onClickRememberPassword = { lviewModel.toggleRememberUser() }
        )
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
}