package com.example.inventariosapp.navigation

sealed class Destinations(val ruta: String) {
    object LoginScreen : Destinations("login_screen")
    object SalesScreen : Destinations("sales_screen")
    object PaymentScreen : Destinations("payment_screen")
    object Products : Destinations("products_screen")
    object NewSale : Destinations("newsale_screen")
    object PenndingSale : Destinations("penndingsale_screen")
    object PrintScreen : Destinations("print_screen")
}