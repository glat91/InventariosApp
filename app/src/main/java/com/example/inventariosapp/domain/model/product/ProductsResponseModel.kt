package com.example.inventariosapp.domain.model.product

import com.example.inventariosapp.database.entity.ProductEntity
import com.google.gson.annotations.SerializedName

data class ProductsResponseModel(
    @SerializedName("productoId") val productoId: Int? = null,
    @SerializedName("codigo") val codigo: String? = null,
    @SerializedName("departamento") val departamento: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("descripcionPresentacion") val descripcionPresentacion: String? = null,
    @SerializedName("estatus") val estatus: String? = null,
    @SerializedName("esActivo") val esActivo: Boolean? = null,
    @SerializedName("costo") val costo: Double? = null,
    @SerializedName("precioVenta1") val precioVenta1: Double? = null,
    @SerializedName("precioVenta2") val precioVenta2: Double? = null,
    @SerializedName("precioVenta3") val precioVenta3: Double? = null,
    @SerializedName("precioVenta4") val precioVenta4: Double? = null,
    @SerializedName("nombreUnidadMedida") val nombreUnidadMedida: String = ""
)

fun ProductsResponseModel.toDb() = ProductEntity(
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
)