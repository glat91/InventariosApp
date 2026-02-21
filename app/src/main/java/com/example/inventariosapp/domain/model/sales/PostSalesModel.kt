package com.example.inventariosapp.domain.model.sales

import com.example.inventariosapp.local.entity.PostSaleEntity
import com.example.inventariosapp.local.entity.PostSaleProductEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class PostSalesModel(
    @SerializedName("ventaId") var ventaId: Int = 0,
    @SerializedName("clienteId") var clienteId: Int? = null,
    @SerializedName("estatusVentaId") var estatusVentaId: Int = 1,
    @SerializedName("esActivo") var esActivo: Boolean = true,
    @SerializedName("NombreCliente") var nombreCliente: String? = null,
    @SerializedName("folio") var folio: String = "",
    @SerializedName("subtotal") var subtotal: Double? = null,
    @SerializedName("Descuento") var descuento: Double? = null,
    @SerializedName("iva") var iva: Double? = null,
    @SerializedName("retencion") var retencion: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("fechaVenta") var fechaVenta: String? = null,
    @SerializedName("FechaVentaFormato") var fechaVentaFormato: String? = null,
    @SerializedName("SucursalId") var sucursalId: Int = 1,
    @SerializedName("AlmacenId") var almacenId: Int = 1,
    @SerializedName("Usuario") var usuario: String? = null,
    @SerializedName("MontoPagado") var montoPagado: Double? = null,
    @SerializedName("MontoPorPagar") var montoPorPagar: Double? = null,
    @SerializedName("tipoPagoId") var tipoPagoId: Int? = null,
    @SerializedName("esFueraDeLinea") var esFueraDeLinea: Boolean = false,
    @SerializedName("origenId") var origenId: Int = 1,
    @SerializedName("tipoConexionId") var tipoConexionId: Int = 1,
    @SerializedName("VentaIdInterno") var ventaIdInterno: Int? = null,
    @SerializedName("Version") var version: Int? = null,

    @SerializedName("ventaProductos") var ventaProductos: ArrayList<PostSaleProductModel> = arrayListOf(),

    @SerializedName("Cliente") var cliente: String? = null,
    @SerializedName("Direccion") var direccion: String = "",
    @SerializedName("UsuarioSesionId") var usuarioSesionId: Int = 1,
    @SerializedName("FechaIngreso") var fechaIngreso: String = "",
    @SerializedName("FechaModifico") var fechaModifico: String? = null,
    @SerializedName("Estatus") var estatus: String? = "",
){

}

fun PostSalesModel.toEntity(id: String = UUID.randomUUID().toString()) =
    PostSaleEntity(
        id = id,
        ventaId = ventaId,
        clienteId = clienteId,
        estatusVentaId = estatusVentaId,
        esActivo = esActivo,
        nombreCliente = nombreCliente,
        folio = folio,
        subtotal = subtotal,
        descuento = descuento,
        iva = iva,
        retencion = retencion,
        total = total,
        fechaVenta = fechaVenta,
        fechaVentaFormato = fechaVentaFormato,
        sucursalId = sucursalId,
        almacenId = almacenId,
        usuario = usuario,
        montoPagado = montoPagado,
        montoPorPagar = montoPorPagar,
        tipoPagoId = tipoPagoId,
        esFueraDeLinea = esFueraDeLinea,
        origenId = origenId,
        tipoConexionId = tipoConexionId,
        ventaIdInterno = ventaIdInterno,
        version = version,
        // productos
        cliente = cliente,
        direccion = direccion,
        usuarioSesionId = usuarioSesionId,
        fechaIngreso = fechaIngreso,
        fechaModifico = fechaModifico,
        estatus = estatus,
    )
fun PostSaleProductModel.toEntity(parentId: String) =
    PostSaleProductEntity(
        postSaleId = parentId,
        ventaProductoId = ventaProductoId,
        ventaId = ventaId,
        productoId = productoId,
        cantidad = cantidad,
        precioVenta = precioVenta,
        costo = costo,
        cantidadSolicitada = cantidadSolicitada,
        ventaIdInterno = ventaIdInterno,
        nombreProducto = nombreProducto,
    )


