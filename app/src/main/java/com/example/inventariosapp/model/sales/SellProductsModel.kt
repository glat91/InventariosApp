package com.example.inventariosapp.model.sales

import com.google.gson.annotations.SerializedName

data class SellProductsModel(
    @SerializedName("VentaProductoId") var VentaProductoId: Int? = null,
    @SerializedName("VentaId") var VentaId: Int? = null,
    @SerializedName("ProductoId") var ProductoId: Int? = null,
    @SerializedName("Cantidad") var Cantidad: Int?  = null,
    @SerializedName("PrecioVenta") var PrecioVenta: Double? = null,
    @SerializedName("Costo") var Costo: Double? = null,
    @SerializedName("CantidadSolicitada") var CantidadSolicitada: Int? = null,
    @SerializedName("VentaIdInterno") var VentaIdInterno: String? = null,
    @SerializedName("Venta") var Venta: String? = null
) {
}