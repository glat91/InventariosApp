package com.example.inventariosapp.ui.view.products

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ProductsScreen(navController: NavHostController) {
    val viewModel: ProductsViewModel = hiltViewModel()

    InventoryView(
        search = viewModel.search,
        onClickBack = { navController.popBackStack() },
        onClickMenu = { viewModel.baseViewModel.openMenu() },
        data = viewModel.getFilter()
    )
}