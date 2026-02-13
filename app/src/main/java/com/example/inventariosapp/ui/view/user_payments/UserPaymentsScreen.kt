package com.example.inventariosapp.ui.view.user_payments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.view.login.LoginViewModel

@Composable
fun UserPaymentsScreen(navController: NavHostController) {
    val viewModel: UserPaymentsViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val cnx = LocalContext.current

    // region Screen
    UserPaymentsView(
        data = viewModel.penndingPayments,
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickUpdate = { viewModel.setPayment() }
    )
    // endregion
    // region dialog
    if (viewModel.baseViewModel.dialogLogin.value){
        LoginDialogCmp(
            user = lviewModel.user,
            password = lviewModel.password,
            rememberUser = remember { mutableStateOf(false) },
            onClickEnter = {
                if (!lviewModel.rememberUser.value) lviewModel.clearUser()
                else lviewModel.saveUserLogin()
                lviewModel.validateUserLogin(MainActivity.internetBtn.value)
            },
            onClickRememberPassword = {
                lviewModel.rememberUser.value = !lviewModel.rememberUser.value
            }
        )
    }
    // endregion
}