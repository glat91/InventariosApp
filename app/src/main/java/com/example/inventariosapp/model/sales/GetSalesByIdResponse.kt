package com.example.inventariosapp.model.sales

import com.example.inventariosapp.database.entity.PostSaleEntity
import com.example.inventariosapp.database.entity.PostSaleProductEntity
import com.example.inventariosapp.model.client.ClientResponseModel
import com.google.gson.annotations.SerializedName
import java.util.UUID

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
    @SerializedName("version") var version: Int? = null,

    @SerializedName("ventaProductos") var ventaProductos: ArrayList<SaleProductModel> = arrayListOf(),
    @SerializedName("cliente") var cliente: ClientResponseModel? = ClientResponseModel(),

    @SerializedName("direccion") var direccion: String? = null,
    @SerializedName("usuarioSesionId") var usuarioSesionId: Int? = null,
    @SerializedName("fechaIngreso") var fechaIngreso: String? = null,
    @SerializedName("fechaModifico") var fechaModifico: String? = null,
    @SerializedName("esActivo") var esActivo: Boolean? = null,
    @SerializedName("estatus") var estatus: String? = null,
)
/*
fun GetSalesByIdResponse.toDb() = PostSaleEntity(
        id = UUID.randomUUID().toString(),
        ventaId = ventaId!!,
        clienteId = clienteId,
        estatusVentaId = estatusVentaId ?: 0,
        esActivo = esActivo ?: true,
        nombreCliente = nombreCliente,
        folio = folio ?: "",
        subtotal = subtotal,
        descuento = descuento,
        iva = iva,
        retencion = retencion ?: 0.0,
        total = total,
        fechaVenta = fechaVenta,
        fechaVentaFormato = fechaVentaFormato,
        sucursalId = sucursalId ?: 0,
        almacenId = almacenId ?: 0,
        usuario = usuario,
        montoPagado = montoPagado,
        montoPorPagar = montoPorPagar,
        tipoPagoId = tipoPagoId,
        esFueraDeLinea = esFueraDeLinea ?: false,
        origenId = origenId ?: 0,
        tipoConexionId = tipoConexionId ?: 1,
        ventaIdInterno = ventaIdInterno,
        version = version,
        // productos
        cliente = cliente?.nombreCliente,
        direccion = direccion ?: "",
        usuarioSesionId = usuarioSesionId,
        fechaIngreso = fechaIngreso ?: "",
        fechaModifico = fechaModifico,
        estatus = estatus,
    )
fun GetSalesByIdResponse.toDb(parentId: String) =
    PostSaleProductEntity(
        postSaleId = parentId,
        ventaProductoId = ventaProductoId,
        ventaId = ventaId ?: 0,
        productoId = productoId,
        cantidad = cantidad,
        precioVenta = precioVenta,
        costo = costo,
        cantidadSolicitada = cantidadSolicitada,
        ventaIdInterno = ventaIdInterno,
        id = parentId,
        nombreProducto = nombreProducto,
    )


 */