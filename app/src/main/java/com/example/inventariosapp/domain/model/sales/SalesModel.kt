package com.example.inventariosapp.domain.model.sales

import android.os.Parcelable
import com.example.inventariosapp.local.entity.SalesEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SalesModel(
    @SerializedName("ventaId") var ventaId: Int? = null,
    @SerializedName("nombreCliente") var nombreCliente: String? = null,
    @SerializedName("folio") var folio: String? = null,
    @SerializedName("subtotal") var subtotal: Double? = null,
    @SerializedName("descuento") var descuento: Int? = null,
    @SerializedName("iva") var iva: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("montoPagado") var montoPagado: Double? = null,
    @SerializedName("montoPorPagar") var montoPorPagar: Double? = null,
    @SerializedName("fechaVenta") var fechaVenta: String? = null,
    @SerializedName("fechaVentaFormato") var fechaVentaFormato: String? = null,
    @SerializedName("estatusVentaId") var estatusVentaId: Int? = null,
    @SerializedName("estatusVenta") var estatusVenta: String? = null,
    @SerializedName("direccion") var direccion: String? = null,
    @SerializedName("diasCredito") var diasCredito: Int? = null,
    @SerializedName("fechaLimitePago") var fechaLimitePago: String? = null
) : Parcelable

fun SalesModel.toDB() = SalesEntity(
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