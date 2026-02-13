package com.example.inventariosapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventariosapp.domain.model.sales.SalesModel

@Entity(tableName = "sales")
data class SalesEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val ventaId: Int?,
    val nombreCliente: String?,
    val folio: String?,
    val subtotal: Double?,
    val descuento: Int?,
    val iva: Double?,
    val total: Double?,
    val montoPagado: Double?,
    val montoPorPagar: Double?,
    val fechaVenta: String?,
    val fechaVentaFormato: String?,
    val estatusVentaId: Int?,
    val estatusVenta: String?,
    val direccion: String?,
    val diasCredito: Int?,
    val fechaLimitePago: String?
)

fun SalesEntity.toDb() = SalesModel(
    ventaId = ventaId,
    nombreCliente = nombreCliente,
    folio = folio,
    subtotal = subtotal,
    descuento = descuento,
    iva = iva,
    total = total,
    montoPagado = montoPagado,
    montoPorPagar = montoPorPagar,
    fechaVenta = fechaVenta,
    fechaVentaFormato = fechaVentaFormato,
    estatusVentaId = estatusVentaId,
    estatusVenta = estatusVenta,
    direccion = direccion,
    diasCredito = diasCredito,
    fechaLimitePago = fechaLimitePago
)