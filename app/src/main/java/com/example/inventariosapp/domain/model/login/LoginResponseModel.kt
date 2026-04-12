package com.example.inventariosapp.domain.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    @SerializedName("usuarioId") var usuarioId : Int? = null,
    @SerializedName("perfilId") var perfilId : Int? = null,
    @SerializedName("perfil") var perfil : String? = null,
    @SerializedName("nombre") var nombre : String? = null,
    @SerializedName("apellidoPaterno") var apellidoPaterno : String? = null,
    @SerializedName("apellidoMaterno") var apellidoMaterno : String? = null,
    @SerializedName("loginName") var loginName : String? = null,
    @SerializedName("contrasenia") var contrasenia : String? = null,
    @SerializedName("correo") var correo : String? = null,
    @SerializedName("esActivo") var esActivo : Boolean? = null,
    @SerializedName("usuarioSesionId") var usuarioSesionId : Int? = null,
    @SerializedName("fechaIngreso") var fechaIngreso : String? = null,
    @SerializedName("fechaModifico") var fechaModifico : String? = null,
    @SerializedName("porcentajeComision") var porcentajeComision : Double? = null
)