package com.example.inventariosapp.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "post_sale_products",
    indices = [Index(value = ["postSaleId"])],
    foreignKeys = [
        ForeignKey(
            entity = PostSaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["postSaleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
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
)