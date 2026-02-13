package com.example.inventariosapp.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventariosapp.domain.model.product.ProductsResponseModel

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo("productoId") val productoId: Int? = null,
    @ColumnInfo("codigo") val codigo: String? = null,
    @ColumnInfo("departamento") val departamento: String? = null,
    @ColumnInfo("descripcion") val descripcion: String? = null,
    @ColumnInfo("descripcionPresentacion") val descripcionPresentacion: String? = null,
    @ColumnInfo("estatus") val estatus: String? = null,
    @ColumnInfo("esActivo") val esActivo: Boolean? = null,
    @ColumnInfo("costo") val costo: Double? = null,
    @ColumnInfo("precioVenta1") val precioVenta1: Double? = null,
    @ColumnInfo("precioVenta2") val precioVenta2: Double? = null,
    @ColumnInfo("precioVenta3") val precioVenta3: Double? = null,
    @ColumnInfo("precioVenta4") val precioVenta4: Double? = null,
    @ColumnInfo("nombreUnidadMedida") val nombreUnidadMedida: String = ""
)

fun ProductEntity.toModel() = ProductsResponseModel(
    productoId = productoId,
    codigo = codigo,
    departamento = departamento,
    descripcion = descripcion,
    descripcionPresentacion = descripcionPresentacion,
    estatus = estatus,
    esActivo = esActivo,
    costo = costo,
    precioVenta1 = precioVenta1,
    precioVenta2 = precioVenta2,
    precioVenta3 = precioVenta3,
    precioVenta4 = precioVenta4,
    nombreUnidadMedida = nombreUnidadMedida
)