package com.example.inventariosapp.domain.model.login

data class LoginRequest(
    val LoginName: String,
    val Contrasenia: String,
    val Manufacturer: String,
    val Brand: String,
    val Model: String,
    val Sdk: String,
    val AndroidVersion: String
)