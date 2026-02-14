package com.example.inventariosapp.ui.view.user_payments

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.view.login.LoginViewModel

@Composable
fun UserPaymentsScreen(navController: NavHostController) {
    val viewModel: UserPaymentsViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()

    // region Screen
    UserPaymentsView(
        data = viewModel.uiState.penndingPayments,
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickUpdate = { viewModel.setPayment() }
    )
    // endregion
    // region dialog
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
}