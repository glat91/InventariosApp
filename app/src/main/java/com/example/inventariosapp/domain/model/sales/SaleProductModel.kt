package com.example.inventariosapp.domain.model.sales

import android.os.Parcelable
import com.example.inventariosapp.domain.model.product.PrecioListaModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaleProductModel(
    @SerializedName("ventaProductoId") var VentaProductoId: Int = 0,
    @SerializedName("ventaId") var VentaId: Int? = null,
    @SerializedName("productoId") var ProductoId: Int? = null,
    @SerializedName("precioVenta") var PrecioVenta: Double? = null,
    @SerializedName("precioLista") var precioLista: ArrayList<PrecioListaModel> = arrayListOf(),
    @SerializedName("cantidadSolicitada") var CantidadSolicitada: Int? = null,
    @SerializedName("costo") var Costo: Double? = null,
    @SerializedName("cantidad") var Cantidad: Int?  = null,
    @SerializedName("ventaIdInterno") var VentaIdInterno: Int? = null,
    @SerializedName("venta") var Venta: String? = null,
    @SerializedName("nombreProducto") var nombreProducto: String? = "",
    @SerializedName("comentarios") var comentarios: String? = "",
) : Parcelable