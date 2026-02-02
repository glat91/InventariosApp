package com.example.inventariosapp.ui.view.user_sales

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.ui.component.Loader

@Composable
fun PenndingSalesScreen(navController: NavHostController) {
    val viewModel: PenndingSalesViewModel = hiltViewModel()

    PenndingSalesView(
        data = viewModel.penndingSales,
        onclickRow = { },
        onClickUpdate = { viewModel.updateSales()},
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        onClickBack = { navController.popBackStack() },
    )

    Loader(viewModel.baseViewModel.getLoader())
}