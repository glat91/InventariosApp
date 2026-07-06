package com.example.inventariosapp.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventariosapp.domain.model.product.InventarioRseponeModel

@Entity(tableName = "inventory")
data class InventoryEntity(
    @PrimaryKey val productoId: Int,
    val producto: String?,
    val almacen: String?,
    val unidadMedida: String?,
    val total: Double?,
    val stockMaximo: Double?,
    val stockMinimo: Double?,
    val msgStockMaximo: String?,
    val msgStockMinimo: String?,
    val msgCaducidad: String?,
    val porCaducar: Double?,
    val inventario: Double?
)

fun InventoryEntity.toModel() = InventarioRseponeModel(
    productoId = productoId,
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