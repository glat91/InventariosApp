package com.example.inventariosapp.model.product

import com.google.gson.annotations.SerializedName

data class PrecioListaModel(
    @SerializedName("precio" ) var precio : Double? = null
)
fun ArrayList<PrecioListaModel>.toDoubleList(): List<Double> {
    return this.map { it.precio ?: 0.0 }
}