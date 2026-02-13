package com.example.inventariosapp.domain.model.sales

import com.google.gson.annotations.SerializedName

data class GetSalesByIdModel(
    @SerializedName("VentaId") var VentaId: Int? = null,
    @SerializedName("ClienteId") var ClienteId: Int? = null,
    @SerializedName("EstatusVentaId") var EstatusVentaId: Int? = null,
    @SerializedName("NombreCliente") var NombreCliente: String? = null,
    @SerializedName("Folio") var Folio: String? = null,
    @SerializedName("Subtotal") var Subtotal: Double? = null,
    @SerializedName("Descuento") var Descuento: Int? = null,
    @SerializedName("IVA") var IVA: Double? = null,
    @SerializedName("Retencion") var Retencion: Int? = null,
    @SerializedName("Total") var Total: Double? = null,
    @SerializedName("FechaVenta") var FechaVenta: String? = null,
    @SerializedName("FechaVentaFormato") var FechaVentaFormato: String? = null,
    @SerializedName("SucursalId") var SucursalId: Int?    = null,
    @SerializedName("AlmacenId") var AlmacenId: Int?    = null,
    @SerializedName("Usuario") var Usuario: String? = null,
    @SerializedName("MontoPagado") var MontoPagado: String? = null,
    @SerializedName("MontoPorPagar") var MontoPorPagar: Double? = null,
    @SerializedName("TipoPagoId") var TipoPagoId: Int?    = null,
    @SerializedName("EsFueraDeLinea") var EsFueraDeLinea: Boolean? = null,
    @SerializedName("OrigenId") var OrigenId: Int? = null,
    @SerializedName("TipoConexionId") var TipoConexionId: Int? = null,
    @SerializedName("VentaIdInterno") var VentaIdInterno: String? = null,
    @SerializedName("Version") var Version: String? = null,
    @SerializedName("VentaProductos") var VentaProductos: ArrayList<SellProductsModel> = arrayListOf(),
    @SerializedName("Cliente") var Cliente: String? = null,
    @SerializedName("Direccion") var Direccion: String? = null,
    @SerializedName("UsuarioSesionId") var UsuarioSesionId: Int? = null,
    @SerializedName("FechaIngreso") var FechaIngreso: String? = null,
    @SerializedName("FechaModifico") var FechaModifico: String? = null,
    @SerializedName("EsActivo") var EsActivo: Boolean? = null,
    @SerializedName("Estatus") var Estatus: String? = null
)