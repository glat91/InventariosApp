package com.example.inventariosapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventariosapp.ui.animations.animatedComposable
import com.example.inventariosapp.ui.view.BluetoothPrinterScreen.BluetoothPrinterScreen
import com.example.inventariosapp.ui.view.login.LoginScreen
import com.example.inventariosapp.ui.view.new_sale.NewSaleScreen
import com.example.inventariosapp.ui.view.products.ProductsScreen
import com.example.inventariosapp.ui.view.payment.PaymentsScreen
import com.example.inventariosapp.ui.view.sales.SalesScreen
import com.example.inventariosapp.ui.view.user_payments.UserPaymentsScreen
import com.example.inventariosapp.ui.view.user_sales.PenndingSalesScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LoginScreen.ruta
    ){
        loginScreen(navController = navController)
        salesScreen(navController = navController)
        paymentScreen(navController = navController)
        productsScreen(navController = navController)
        newSaleScreen(navController = navController)
        penndingSaleScreen(navController = navController)
        penndingPaymentsScreen(navController = navController)
        bluetoothPrinterScreen(navController = navController)
    }
}

// region Screen
fun NavGraphBuilder.loginScreen(navController: NavHostController){
    animatedComposable(
        route = Destinations.LoginScreen.ruta,
        durationMillis = 600,
    ){
        LoginScreen(navController)
    }
}
fun NavGraphBuilder.salesScreen(navController: NavHostController){
    animatedComposable(
         route = Destinations.SalesScreen.ruta,
         durationMillis = 600,
     ) {
        SalesScreen(navController)
     }

}

fun NavGraphBuilder.paymentScreen(navController: NavHostController){
    animatedComposable(
        route = Destinations.PaymentScreen.ruta,
        durationMillis = 600,
    ){
        PaymentsScreen(navController)
    }
}
fun NavGraphBuilder.productsScreen(navController: NavHostController){
    animatedComposable(
        route = Destinations.ProductsScreen.ruta,
        durationMillis = 600,
    ){
        ProductsScreen(navController)
    }
}
fun NavGraphBuilder.newSaleScreen(navController: NavHostController){
    animatedComposable(
        route = Destinations.NewSaleScreen.ruta,
        durationMillis = 600,
    ){
        NewSaleScreen(navController)
    }
}
fun NavGraphBuilder.penndingSaleScreen(navController: NavHostController){
    animatedComposable(
        route = Destinations.PenndingSaleScreen.ruta,
        durationMillis = 600,
    ){
        PenndingSalesScreen(navController)
    }
}
fun NavGraphBuilder.penndingPaymentsScreen(navController: NavHostController){
    animatedComposable(
        route = Destinations.PenndingPaymentsScreen.ruta,
        durationMillis = 600,
    ){
        UserPaymentsScreen(navController)
    }
}
@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.bluetoothPrinterScreen(navController: NavHostController){
    animatedComposable(
        route = Destinations.PrintScreen.ruta,
        durationMillis = 600,
    ){
        BluetoothPrinterScreen(navController)
    }
}
// endregion