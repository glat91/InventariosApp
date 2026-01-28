package com.example.inventariosapp.model.product

import com.google.gson.annotations.SerializedName

data class ProductIdResponseModel(
    @SerializedName("productoId") val productoId: Int? = null,
    @SerializedName("inventario") val inventario: Int? = null,
)