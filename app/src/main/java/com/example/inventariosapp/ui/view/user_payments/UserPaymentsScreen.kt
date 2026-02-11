package com.example.inventariosapp.ui.view.user_payments

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun UserPaymentsScreen(navController: NavHostController) {
    val viewModel: UserPaymentsViewModel = hiltViewModel()
    // region Screen
    UserPaymentsView(
        data = viewModel.penndingPayments,
        onclickRow = {},
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickUpdate = {}
    )
    // endregion
    // region dialog

    // endregion
}