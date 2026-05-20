package com.example.inventariosapp.domain.model.sales

import com.example.inventariosapp.local.entity.PostSaleProductEntity
import com.google.gson.annotations.SerializedName

data class PostSaleProductModel(
    @SerializedName("VentaProductoId") var ventaProductoId: Int = 0,
    @SerializedName("ventaId") var ventaId: Int = 0,
    @SerializedName("productoId") var productoId: Int? = null,
    @SerializedName("cantidad") var cantidad: Int? = null,
    @SerializedName("precioVenta") var precioVenta: Double? = null,
    @SerializedName("costo") var costo: Double? = null,
    @SerializedName("CantidadSolicitada") var cantidadSolicitada: Int = 0,
    @SerializedName("VentaIdInterno") var ventaIdInterno: Int? = 0,
    @SerializedName("nombreProducto") var nombreProducto: String? = "",
    @SerializedName("comentarios") var comentarios: String? = "",
    )

fun PostSaleProductModel.toDB(id: String) =
    PostSaleProductEntity(
        id = id,
        cantidad = cantidad,
        productoId = productoId,
        precioVenta = precioVenta,
        costo = costo,
        ventaId = ventaId,
        postSaleId = id,
        ventaProductoId = ventaProductoId,
        cantidadSolicitada = cantidadSolicitada,
        ventaIdInterno = ventaIdInterno,
        nombreProducto = nombreProducto ?: "",
        comentarios = comentarios ?: ""
    )