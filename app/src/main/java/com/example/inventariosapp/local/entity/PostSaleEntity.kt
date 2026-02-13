package com.example.inventariosapp.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventariosapp.domain.model.sales.PostSaleProductModel
import com.example.inventariosapp.domain.model.sales.PostSalesModel
import java.util.UUID

@Entity(tableName = "post_sales")
data class PostSaleEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var ventaId: Int = 0,
    var clienteId: Int? = null,
    var estatusVentaId: Int = 1,
    var esActivo: Boolean = true,
    var nombreCliente: String? = null,
    var folio: String = "",
    var subtotal: Double? = null,
    var descuento: Double? = null,
    var iva: Double? = null,
    var retencion: Double? = null,
    var total: Double? = null,
    var fechaVenta: String? = null,
    var fechaVentaFormato: String? = null,
    var sucursalId: Int = 1,
    var almacenId: Int = 1,
    var usuario: String? = null,
    var montoPagado: Double? = null,
    var montoPorPagar: Double? = null,
    var tipoPagoId: Int? = null,
    var esFueraDeLinea: Boolean = false,
    var origenId: Int = 1,
    var tipoConexionId: Int = 1,
    var ventaIdInterno: Int? = null,
    var version: Int? = null,

    var cliente: String? = null,
    var direccion: String = "",
    var usuarioSesionId: Int? = null,
    var fechaIngreso: String = "",
    var fechaModifico: String? = null,
    var estatus: String? = "",
)

fun PostSaleEntity.toModel(productos: List<PostSaleProductModel>) = PostSalesModel(
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
    fechaIngreso = fechaIngreso,
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
    direccion = direccion,
    ventaProductos = productos as ArrayList<PostSaleProductModel>
)

fun PostSaleProductEntity.toModel() = PostSaleProductModel(
    ventaProductoId = ventaProductoId,
    ventaId = ventaId,
    productoId = productoId,
    cantidad = cantidad,
    precioVenta = precioVenta,
    costo = costo,
    cantidadSolicitada = cantidadSolicitada,
    ventaIdInterno = ventaIdInterno,
    nombreProducto = nombreProducto
)

fun PostSaleWithProducts.toModel(): PostSalesModel {
    val productosModel = productos.map { it.toModel() }
    return sale.toModel(productos = productosModel)
}