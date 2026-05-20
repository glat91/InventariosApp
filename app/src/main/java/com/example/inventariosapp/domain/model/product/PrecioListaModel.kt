package com.example.inventariosapp.domain.model.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrecioListaModel(
    @SerializedName("precio" ) var precio : Double? = null
) : Parcelable

fun ArrayList<PrecioListaModel>.toDoubleList(): List<Double> {
    return this.map { it.precio ?: 0.0 }
}