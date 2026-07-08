package com.example.inventariosapp.ui.view.user_sales

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.ui.component.cards.CardSellProductCmp
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.dialog.PenddingSaleDialogCmp
import com.example.inventariosapp.ui.view.login.LoginViewModel
import com.example.inventariosapp.util.Helpers

@Composable
fun PenndingSalesScreen(navController: NavHostController) {
    val viewModel: PenndingSalesViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val cnx = LocalContext.current
    val luiState by lviewModel.uiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    PenndingSalesView(
        data = uiState.penndingSales,
        btnEnabled = uiState.enableBtn,
        onclickRow = {
            viewModel.updateSelectedPenndigSale(it)
            viewModel.updateDialogProduct(true)
        },
        onClickUpdate = { viewModel.updateSales() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickBack = { navController.popBackStack() },
    )

    // region dialog
    if (viewModel.baseViewModel.dialogLogin.value){
        LoginDialogCmp(
            uiState = luiState,
            onClickEnter = {
                val internetUse = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
                if (!luiState.rememberUser) lviewModel.clearUser()
                else lviewModel.saveUserLogin()
                lviewModel.validateUserLogin(internetUse)
            },
            onUserChange = { lviewModel.updateUser(it) },
            onPasswordChange = { lviewModel.updatePassword(it) },
            onClickRememberPassword = { lviewModel.toggleRememberUser() }
        )
    }
    // endregion

    if (uiState.dialogProduct){
        PenddingSaleDialogCmp(
            content = {
                if (uiState.selectedPenndigSale != null){
                    for (product in uiState.selectedPenndigSale!!.productos){
                        CardSellProductCmp(
                            producto = product.nombreProducto,
                            quantity = product.cantidad.toString(),
                            sellPrice = product.precioVenta.toString(),
                            deleteIcon = false,
                            onClickDelete = {}
                        )
                    }

                }
            },
            onDismiss = { viewModel.updateDialogProduct(false) }
        )
    }
    Loader(viewModel.baseViewModel.getLoader())
}