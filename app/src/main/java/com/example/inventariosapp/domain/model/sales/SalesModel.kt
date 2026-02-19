package com.example.inventariosapp.domain.model.sales

import android.os.Parcel
import android.os.Parcelable
import com.example.inventariosapp.local.entity.SalesEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Double    ,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(ventaId)
        parcel.writeString(nombreCliente)
        parcel.writeString(folio)
        parcel.writeValue(subtotal)
        parcel.writeValue(descuento)
        parcel.writeValue(iva)
        parcel.writeValue(total)
        parcel.writeValue(montoPagado)
        parcel.writeValue(montoPorPagar)
        parcel.writeString(fechaVenta)
        parcel.writeString(fechaVentaFormato)
        parcel.writeValue(estatusVentaId)
        parcel.writeString(estatusVenta)
        parcel.writeString(direccion)
        parcel.writeValue(diasCredito)
        parcel.writeString(fechaLimitePago)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SalesModel> {
        override fun createFromParcel(parcel: Parcel): SalesModel {
            return SalesModel(parcel)
        }

        override fun newArray(size: Int): Array<SalesModel?> {
            return arrayOfNulls(size)
        }
    }
}

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