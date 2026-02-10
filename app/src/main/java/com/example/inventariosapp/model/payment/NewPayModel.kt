package com.example.appgeneric.model.payment

import com.example.inventariosapp.database.entity.NewPayEntity
import com.google.gson.annotations.SerializedName

data class NewPayModel(
    @SerializedName("ventaId") var ventaId: Int = 0,
    @SerializedName("montoPago") var montoPago: Double = 0.00,
    @SerializedName("fecha") var fecha: String = "",
    @SerializedName("observaciones") var observaciones: String = "",
    @SerializedName("origenId") var origenId: Int = 0,
    @SerializedName("tipoConexionId") var tipoConexionId: Int = 0,
    @SerializedName("usuarioSesionId") var usuarioSesionId: Int = 0,
)

fun NewPayModel.toDb() = NewPayEntity(
    ventaId = ventaId,
    montoPago = montoPago,
    fecha = fecha,
    observaciones = observaciones,
    tipoConexionId = tipoConexionId,
)