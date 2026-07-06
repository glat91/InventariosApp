package com.example.inventariosapp.domain.model.product

import com.example.inventariosapp.local.entity.InventoryEntity
import com.google.gson.annotations.SerializedName

data class InventarioRseponeModel(
    @SerializedName("productoId") var productoId: Int? = null,
    @SerializedName("producto") var producto: String? = null,
    @SerializedName("almacen") var almacen: String? = null,
    @SerializedName("unidadMedida") var unidadMedida: String? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("stockMaximo") var stockMaximo: Double? = null,
    @SerializedName("stockMinimo") var stockMinimo: Double? = null,
    @SerializedName("msgStockMaximo") var msgStockMaximo: String? = null,
    @SerializedName("msgStockMinimo") var msgStockMinimo: String? = null,
    @SerializedName("msgCaducidad") var msgCaducidad: String? = null,
    @SerializedName("porCaducar") var porCaducar: Double? = null,
    @SerializedName("inventario") var inventario: Double? = null
)

fun InventarioRseponeModel.toDb() = InventoryEntity(
    productoId = productoId ?: 0,
    producto = producto,
    almacen = almacen,
    unidadMedida = unidadMedida,
    total = total,
    stockMaximo = stockMaximo,
    stockMinimo = stockMinimo,
    msgStockMaximo = msgStockMaximo,
    msgStockMinimo = msgStockMinimo,
    msgCaducidad = msgCaducidad,
    porCaducar = porCaducar,
    inventario = inventario
)