package com.example.inventariosapp.ui.view.user_payments

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.dialog.LoginDialogCmp
import com.example.inventariosapp.ui.view.login.LoginViewModel
import com.example.inventariosapp.util.Helpers

@Composable
fun UserPaymentsScreen(navController: NavHostController) {
    val viewModel: UserPaymentsViewModel = hiltViewModel()
    val lviewModel: LoginViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val cnx = LocalContext.current
    val uiState by lviewModel.uiState.collectAsState()

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getPenndingPayments()
        }
    }

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
}