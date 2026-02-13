package com.example.inventariosapp.domain.model.sales

import com.google.gson.annotations.SerializedName

data class GetPaymentResponseModel(
    @SerializedName("tipoPagoId") val tipoPagoId: Int? = null,
    @SerializedName("nombreTipoPago") val nombreTipoPago: String? = null,
    @SerializedName("esActivo") val esActivo: Boolean? = null,
)