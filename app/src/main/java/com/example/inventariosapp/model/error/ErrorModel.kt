package com.example.inventariosapp.model.error

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ErrorModel(
    @SerializedName("MsgError") var MsgError: MsgErrorModel? = MsgErrorModel()
) : Serializable