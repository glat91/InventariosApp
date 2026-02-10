package com.example.inventariosapp.model.payment

import com.example.inventariosapp.database.entity.PayEntity
import com.google.gson.annotations.SerializedName


data class PayModel(
    @SerializedName("ventaPagoId") var ventaPagoId: Int? = null,
    @SerializedName("ventaId") var ventaId: Int? = null,
    @SerializedName("montoPago") var montoPago: Double = 0.00,
    @SerializedName("fecha") var fecha: String? = null,
    @SerializedName("observaciones") var observaciones: String? = null,
    @SerializedName("origenId") var origenId: Int? = null,
    @SerializedName("tipoConexionId") var tipoConexionId: Int? = null,
    @SerializedName("nombreVendedor") var nombreVendedor: String? = null,
    @SerializedName("usuarioSesionId") var usuarioSesionId: Int? = null,
    @SerializedName("fechaIngreso") var fechaIngreso: String? = null,
    @SerializedName("fechaModifico") var fechaModifico: String? = null,
    @SerializedName("esActivo") var esActivo: Boolean? = null,
    @SerializedName("estatus") var estatus: String? = null
)

fun PayModel.toDB() = PayEntity(
    ventaPagoId = ventaPagoId ?: 0,
    ventaId = ventaId,
    montoPago = montoPago,
    fecha = fecha,
    observaciones = observaciones,
    origenId = origenId,
    tipoConexionId = tipoConexionId,
    nombreVendedor = nombreVendedor,
    usuarioSesionId = usuarioSesionId,
    fechaIngreso = fechaIngreso,
    fechaModifico = fechaModifico,
    esActivo = esActivo,
    estatus = estatus
)