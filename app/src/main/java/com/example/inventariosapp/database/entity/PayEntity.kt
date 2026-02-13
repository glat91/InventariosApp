package com.example.inventariosapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventariosapp.domain.model.payment.PayModel

@Entity(tableName = "pay_table")
data class PayEntity(
    @PrimaryKey
    val ventaPagoId: Int,
    val ventaId: Int?,
    val montoPago: Double?,
    val fecha: String?,
    val observaciones: String?,
    val origenId: Int?,
    val tipoConexionId: Int?,
    val nombreVendedor: String?,
    val usuarioSesionId: Int?,
    val fechaIngreso: String?,
    val fechaModifico: String?,
    val esActivo: Boolean?,
    val estatus: String?
)

fun PayEntity.toDB() = PayModel(
    ventaPagoId = ventaPagoId,
    ventaId = ventaId,
    montoPago = montoPago ?: 0.00,
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