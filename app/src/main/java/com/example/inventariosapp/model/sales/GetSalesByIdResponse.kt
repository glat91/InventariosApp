package com.example.inventariosapp.model.sales

import com.example.inventariosapp.model.client.ClientResponseModel
import com.google.gson.annotations.SerializedName

data class GetSalesByIdResponse(
    @SerializedName("ventaId") var ventaId: Int? = null,
    @SerializedName("clienteId") var clienteId: Int? = null,
    @SerializedName("estatusVentaId") var estatusVentaId: Int? = null,
    @SerializedName("nombreCliente") var nombreCliente: String? = null,
    @SerializedName("folio") var folio: String? = null,
    @SerializedName("subtotal") var subtotal: Double? = null,
    @SerializedName("descuento") var descuento: Double? = null,
    @SerializedName("iva") var iva: Double? = null,
    @SerializedName("retencion") var retencion: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("fechaVenta") var fechaVenta: String? = null,
    @SerializedName("fechaVentaFormato") var fechaVentaFormato: String? = null,
    @SerializedName("sucursalId") var sucursalId: Int? = null,
    @SerializedName("almacenId") var almacenId: Int? = null,
    @SerializedName("usuario") var usuario: String? = null,
    @SerializedName("montoPagado") var montoPagado: Double? = null,
    @SerializedName("montoPorPagar") var montoPorPagar: Double? = null,
    @SerializedName("tipoPagoId") var tipoPagoId: Int? = null,
    @SerializedName("esFueraDeLinea") var esFueraDeLinea: Boolean? = null,
    @SerializedName("origenId") var origenId: Int? = null,
    @SerializedName("tipoConexionId") var tipoConexionId: Int? = null,
    @SerializedName("ventaIdInterno") var ventaIdInterno: Int? = null,
    @SerializedName("version") var version: String? = null,

    @SerializedName("ventaProductos") var ventaProductos: ArrayList<SaleProductModel> = arrayListOf(),
    @SerializedName("cliente") var cliente: ClientResponseModel? = ClientResponseModel(),

    @SerializedName("direccion") var direccion: String? = null,
    @SerializedName("usuarioSesionId") var usuarioSesionId: Int? = null,
    @SerializedName("fechaIngreso") var fechaIngreso: String? = null,
    @SerializedName("fechaModifico") var fechaModifico: String? = null,
    @SerializedName("esActivo") var esActivo: Boolean? = null,
    @SerializedName("estatus") var estatus: String? = null,
)
