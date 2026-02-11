package com.example.inventariosapp.navigation

sealed class Destinations(val ruta: String) {
    object LoginScreen : Destinations("login_screen")
    object SalesScreen : Destinations("sales_screen")
    object PaymentScreen : Destinations("payment_screen")
    object ProductsScreen : Destinations("products_screen")
    object NewSaleScreen : Destinations("newsale_screen")
    object PenndingSaleScreen : Destinations("penndingsale_screen")
    object PrintScreen : Destinations("print_screen")
    object PenndingPaymentsScreen : Destinations("pennding_payments_screen")
}