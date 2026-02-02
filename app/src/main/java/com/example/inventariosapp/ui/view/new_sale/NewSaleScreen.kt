package com.example.inventariosapp.ui.view.new_sale

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.navigation.Destinations
import com.example.inventariosapp.ui.component.Loader
import com.example.inventariosapp.ui.dialog.AddProductDialogCmp

@Composable
fun availableDropdownHeight(): Dp {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val imeHeight = with(density) { WindowInsets.ime.getBottom(this).toDp() }

    return screenHeight - imeHeight - 120.dp
}

@Composable
fun NewSaleScreen(navController: NavHostController) {
    val viewModel: NewSaleViewModel = hiltViewModel()
    val editStatus = viewModel.editStatus.collectAsState()
    val postSale = viewModel.serverPostSale.collectAsState()

    // region Previous Data
    LaunchedEffect(true){
        Log.i("Modify___", viewModel.canModifyClient.toString())
        try{
            viewModel.sale.value = navController.previousBackStackEntry?.savedStateHandle?.get<SalesModel>("sale")!!
            viewModel.getSale()
        }
        catch (e: Exception){ }
        viewModel.client.value = TextFieldValue(viewModel.sale.value.nombreCliente ?: "")
        Log.i("Modify___", viewModel.canModifyClient.toString())
    }
    // endregion
    LaunchedEffect(editStatus.value){
        if (editStatus.value){
            navController.navigate(route = Destinations.SalesScreen.ruta){
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(postSale.value) {
        if (postSale.value){
            navController.navigate(route = Destinations.SalesScreen.ruta){
                launchSingleTop = true
                popUpTo(Destinations.NewSale.ruta){ inclusive = true }
            }
        }
    }
    // region Composable
    NewSaleView(
        clientName = viewModel.client,
        sale = viewModel.sale,
        salesData = viewModel.products,
        expandedSearchBar = viewModel.expandenSearchBarS,
        opcions = viewModel.filterClients(),
        canModify = viewModel.canModifyClient,
        onClickOpcion = {
            Log.i("Modify___", viewModel.canModifyClient.toString())
            if (viewModel.canModifyClient.value) {
                viewModel.newClient.value = it
                viewModel.client.value = TextFieldValue(it.nombreCliente.toString())
                viewModel.sale.value.nombreCliente = it.nombreCliente.toString()
                viewModel.expandenSearchBarS.value = false
            }
        },
        onClickDelete = { viewModel.deleteRow(it) },
        onClickProduct = { viewModel.dialogProduct.value = true },
        onClickSave = {
            if (viewModel.sale.value.ventaId == null) {
                if (viewModel.products.isNotEmpty() && viewModel.newClient.value != null) {
                    viewModel.createSale()
                }
            }
            else {
                if (viewModel.products.isNotEmpty()){ viewModel.editSale() }
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
    if (viewModel.dialogProduct.value){
        AddProductDialogCmp(
            state = viewModel.search,
            opcions = viewModel.getFilter().value,
            expanded = viewModel.expandenSearchBarD,
            product = viewModel.selectedProduct,
            quantity = viewModel.quantity,
            onDismiss = {
                viewModel.dialogProduct.value = false
                viewModel.opcions.value.clear()
                viewModel.product.value = null
                //viewModel.inventory.value = null
                viewModel.selectedProduct.value = null
            },
            onChangeText = { viewModel.search.value = it },
            onClickOpcion = {
                Log.i("Opcion___", it.toString())
                viewModel.selectedProduct.value = it
                viewModel.expandenSearchBarD.value = false
                viewModel.getProductInventario(it.productoId!!)
            },
            onClickPrice = { viewModel.price.value = it },
            inventario = viewModel.totalInventory.value,
            onClickCancel = {
                viewModel.dialogProduct.value = false
                viewModel.search.value = TextFieldValue("")
                viewModel.opcions.value.clear()
                viewModel.product.value = null
                viewModel.expandenSearchBarD.value = false
                //viewModel.inventory.value = null
                viewModel.selectedProduct.value = null
            },
            onClickAccept = {
                viewModel.addRow(it)
                viewModel.dialogProduct.value = false
                viewModel.search.value = TextFieldValue("")
                viewModel.opcions.value.clear()
                viewModel.product.value = null
                viewModel.expandenSearchBarD.value = false
                //viewModel.inventory.value = null
                viewModel.selectedProduct.value = null

            }
        )
    }
    // endregion
    Loader(viewModel.baseViewModel.getLoader())
}