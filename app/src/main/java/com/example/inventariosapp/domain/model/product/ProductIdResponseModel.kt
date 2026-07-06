package com.example.inventariosapp.domain.model.product

import com.google.gson.annotations.SerializedName

data class ProductIdResponseModel(
    @SerializedName("productoId") val productoId: Int? = null,
    @SerializedName("inventario") val inventario: Double? = null,
)