package com.example.inventariosapp.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appgeneric.model.payment.NewPayModel

@Entity
data class NewPayEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ventaId: Int = 0, // ventaId clave primaria back
    var montoPago: Double = 0.0,
    var fecha: String = "",
    var observaciones: String = "",
    var origenId: Int = 0,
    var tipoConexionId: Int = 0,
    var usuarioSesionId: Int = 0,
)

fun NewPayEntity.toModel() = NewPayModel(
    ventaId = ventaId,
    montoPago = montoPago,
    fecha = fecha,
    observaciones = observaciones,
    origenId = origenId,
    tipoConexionId = tipoConexionId,
    usuarioSesionId = usuarioSesionId,
)