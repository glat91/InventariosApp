package com.example.appgeneric.model.payment

import com.google.gson.annotations.SerializedName

data class NewPayModel(
    @SerializedName("ventaId") var ventaId: Int = 0,
    @SerializedName("montoPago") var montoPago: Double = 0.0,
    @SerializedName("fecha") var fecha: String = "",
    @SerializedName("observaciones") var observaciones: String = "",
    @SerializedName("origenId") var origenId: Int = 0,
    @SerializedName("tipoConexionId") var tipoConexionId: Int = 0,
    @SerializedName("usuarioSesionId") var usuarioSesionId: Int = 0,
)