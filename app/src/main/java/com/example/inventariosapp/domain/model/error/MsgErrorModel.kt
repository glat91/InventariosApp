package com.example.inventariosapp.domain.model.error

import com.google.gson.annotations.SerializedName

data class MsgErrorModel(
    @SerializedName("rawValue") var rawValue: String? = null,
    @SerializedName("attemptedValue") var attemptedValue: String? = null,
    @SerializedName("errors") var errors: List<ErrorsModel>? = null,
    @SerializedName("validationState") var validationState: Int? = null,
    @SerializedName("isContainerNode") var isContainerNode: Boolean? = null,
    @SerializedName("children") var children: String? = null
)