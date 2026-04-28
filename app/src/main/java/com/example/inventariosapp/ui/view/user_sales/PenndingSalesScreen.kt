package com.example.inventariosapp.ui.view.user_sales

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.ui.component.cards.CardPenndingProductCmp
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.dialog.PenddingSaleDialogCmp
import com.example.inventariosapp.ui.view.login.LoginViewModel
import com.example.inventariosapp.util.Helpers

@Composable
fun PenndingSalesScreen(navController: NavHostController) {
    val viewModel: PenndingSalesViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val cnx = LocalContext.current
    val uiState by lviewModel.uiState.collectAsState()

    PenndingSalesView(
        data = viewModel.penndingSales,
        onclickRow = {
            viewModel.selectedPenndigSale.value = it
            viewModel.dialogProduct.value = true
        },
        onClickUpdate = { viewModel.updateSales()},
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickBack = { navController.popBackStack() },
    )

    // region dialog
    if (viewModel.baseViewModel.dialogLogin.value){
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

    PenddingSaleDialogCmp(
        content = {
            if (viewModel.selectedPenndigSale.value != null){
                for (product in viewModel.selectedPenndigSale.value!!.productos){
                    CardPenndingProductCmp(
                        backgroundColor = Color.Transparent,
                        producto = product.nombreProducto,
                        quantity = product.cantidadSolicitada.toString(),
                        sellPrice = product.precioVenta.toString(),
                    )
                }

            }
        },
        onDismiss = { viewModel.dialogProduct.value = false}
    )

    Loader(viewModel.baseViewModel.getLoader())
}