package com.example.inventariosapp.ui.view.user_sales

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.view.login.LoginViewModel

@Composable
fun PenndingSalesScreen(navController: NavHostController) {
    val viewModel: PenndingSalesViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val cnx = LocalContext.current

    PenndingSalesView(
        data = viewModel.penndingSales,
        onclickRow = { },
        onClickUpdate = { viewModel.updateSales()},
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickBack = { navController.popBackStack() },
    )

    // region dialog
    if (viewModel.baseViewModel.dialogLogin.value){
        LoginDialogCmp(
            user = lviewModel.user,
            password = lviewModel.password,
            rememberUser = remember { mutableStateOf(false) },
            onClickEnter = {
                if (!lviewModel.rememberUser.value) lviewModel.clearUser(cnx)
                else lviewModel.saveUserLogin(cnx)
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