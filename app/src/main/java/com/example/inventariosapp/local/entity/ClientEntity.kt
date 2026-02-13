package com.example.inventariosapp.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inventariosapp.domain.model.client.ClientResponseModel

@Entity(tableName = "client_table")
data class ClientEntity(
    @PrimaryKey
    @ColumnInfo("clienteId")val clienteId: Int,
    @ColumnInfo("tipoPersonaId") val tipoPersonaId: Int?,
    @ColumnInfo("tipoPersona") val tipoPersona: String?,
    @ColumnInfo("tipoGiroId") val tipoGiroId: Int?,
    @ColumnInfo("tipoGiro") val tipoGiro: String?,
    @ColumnInfo("tipoPagoId") val tipoPagoId: Int?,
    @ColumnInfo("tipoPago") val tipoPago: String?,
    @ColumnInfo("nombreCliente") val nombreCliente: String?,
    @ColumnInfo("razonSocial") val razonSocial: String?,
    @ColumnInfo("rfc") val rfc: String?,
    @ColumnInfo("curp") val curp: String?,
    @ColumnInfo("paisId") val paisId: Int?,
    @ColumnInfo("estadoId") val estadoId: Int?,
    @ColumnInfo("ciudadId") val ciudadId: Int?,
    @ColumnInfo("localidad") val localidad: String?,
    @ColumnInfo("direccion") val direccion: String?,
    @ColumnInfo("colonia") val colonia: String?,
    @ColumnInfo("calle") val calle: String?,
    @ColumnInfo("codigoPostal") val codigoPostal: String?,
    @ColumnInfo("noExterior") val noExterior: String?,
    @ColumnInfo("noInterior") val noInterior: String?,
    @ColumnInfo("telefono") val telefono: String?,
    @ColumnInfo("correo") val correo: String?,
    @ColumnInfo("montoCredito") val montoCredito: Double?,
    @ColumnInfo("diasCredito") val diasCredito: Int?,
    @ColumnInfo("usuarioSesionId") val usuarioSesionId: Int?,
    @ColumnInfo("fechaIngreso") val fechaIngreso: String?,
    @ColumnInfo("fechaModifico") val fechaModifico: String?,
    @ColumnInfo("esActivo") val esActivo: Boolean?,
    @ColumnInfo("estatus") val estatus: String?
)
fun ClientEntity.toModel() = ClientResponseModel(
    clienteId = clienteId,
    tipoPersonaId = tipoPersonaId,
    tipoPersona = tipoPersona,
    tipoGiroId = tipoGiroId,
    tipoGiro = tipoGiro,
    tipoPagoId = tipoPagoId,
    tipoPago = tipoPago,
    nombreCliente = nombreCliente,
    razonSocial = razonSocial,
    rfc = rfc,
    curp = curp,
    paisId = paisId,
    estadoId = estadoId,
    ciudadId = ciudadId,
    localidad = localidad,
    direccion = direccion,
    colonia = colonia,
    calle = calle,
    codigoPostal = codigoPostal,
    noExterior = noExterior,
    noInterior = noInterior,
    telefono = telefono,
    correo = correo,
    montoCredito = montoCredito,
    diasCredito = diasCredito,
    usuarioSesionId = usuarioSesionId,
    fechaIngreso = fechaIngreso,
    fechaModifico = fechaModifico,
    esActivo = esActivo,
    estatus = estatus
)