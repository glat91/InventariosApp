package com.example.inventariosapp.domain.model.error

import com.google.gson.annotations.SerializedName

data class ErrorsModel(
    @SerializedName("exception") var exception: String? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null
)