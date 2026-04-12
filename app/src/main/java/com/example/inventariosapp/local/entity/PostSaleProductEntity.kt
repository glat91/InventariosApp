package com.example.inventariosapp.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "post_sale_products",
    foreignKeys = [
        ForeignKey(
            entity = PostSaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["postSaleId"], // 🔥 ESTE ES EL FIX
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["postSaleId"])]
)
data class PostSaleProductEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val postSaleId: String,
    var ventaProductoId: Int = 0,
    var ventaId: Int = 0,
    var productoId: Int? = null,
    var cantidad: Int? = null,
    var precioVenta: Double? = null,
    var costo: Double? = null,
    var cantidadSolicitada: Int = 0,
    var ventaIdInterno: Int? = 0,
    var nombreProducto: String,
    var comentarios: String? = null,
)