Inventario API – Guía de Homologación (Swagger → Android)
Última actualización: 2025-04-05 | Versión Swagger: 2.0

================================================================================
ENDPOINTS PRINCIPALES
================================================================================

/api/almacen (GET)
--------------------------------------------------------------------------------
Descripción: Listado de almacenes con filtros múltiples.
Parámetros Query (todos opcionales):
- TextoBusqueda: string
- Folio: string
- EsActivo: boolean
- TipoMovimientoId: int32
- FechaInicio, FechaFin, FechaSincronizacion: datetime (formato ISO: yyyy-MM-ddTHH:mm:ss)
- ClienteId, ProductoId, Anio, VentaId, EstatusVentaId: int32
- EstatusVentaIds: string (ej. "1,2,3")

Response: AlmacenDTO[]

AlmacenDTO (campos obligatorios):
- almacenId: int32
- nombreAlmacen: string (max 100)
- sucursalId: int32
- esActivo: boolean
- fechaIngreso: datetime
- usuarioSesionId: int32

Nota: Campo "sucursal" en respuesta es string (nombre concatenado), no objeto.

--------------------------------------------------------------------------------
/api/almacen (POST)
Body: AlmacenDTO
Response: 201 Created

--------------------------------------------------------------------------------
/api/almacen/{AlmacenId} (GET, PUT, DELETE)
Path param: AlmacenId (int32, obligatorio)

Responses:
- GET: 200 + AlmacenDTO
- PUT: 204 No Content
- DELETE: 204 o 409 Conflict

--------------------------------------------------------------------------------
/api/almacen/export (POST)
Parámetros: Igual que GET (query params).
Response: Archivo binario (Excel/PDF), NO JSON.

================================================================================

/api/ciudad (GET)
Query:
- PaisId: int32
- EstadoId: int32
  Response: CiudadDTO[]

CiudadDTO:
- ciudadID: int32 (¡ojo! con "D" mayúscula en ID, inconsistente con otros DTOs)
- nombreCiudad: string (max 100)

================================================================================

/api/Cliente (GET, POST)
Query: Mismos parámetros que /api/almacen (13+ filtros comunes).
Response GET: ClienteDTO[]

ClienteDTO (campos obligatorios):
- clienteId: int32
- tipoPersonaId, tipoGiroId, tipoPagoId: int32
- esActivo: boolean
- fechaIngreso: datetime
- usuarioSesionId: int32
- direccion: string (max 250)
- calle: string (max 200)
- ciudadId, estadoId, paisId: int32
- codigoPostal: string (max 25)

--------------------------------------------------------------------------------
/api/Cliente/export (POST)
Igual que /api/almacen/export.

================================================================================

/api/Producto (GET, POST)
Response GET: ProductoDTO[]
ProductoDTO (campos obligatorios):
- productoId: int32
- descripcion: string (max 300)
- presentacion: string (max 100)
- unidadMedidaId: int32
- costo: double
- precioVenta1: double
- esActivo, fechaIngreso, usuarioSesionId: boolean/datetime/int32

================================================================================

/api/Venta (GET, POST)
Response GET: VentaDTO[]

VentaDTO (campos obligatorios):
- ventaId: int32
- clienteId, estatusVentaId: int32
- subtotal, descuento, iva, retencion, total: double
- fechaVenta: datetime (¡formato ISO! yyyy-MM-ddTHH:mm:ss)
- usuarioSesionId: int32
- esActivo: boolean

POST Body: VentaDTO[] (array de ventas)

================================================================================

/api/VentaPago (GET, POST)
Response GET: VentaPagoDTO[]

VentaPagoDTO:
- ventaPagoId, ventaId: int32
- montoPago: double
- fecha: datetime
- usuarioSesionId: int32

--------------------------------------------------------------------------------
/api/VentaPago/{VentaPagoId} (GET, DELETE)
Path param: VentaPagoId (int32, obligatorio)

================================================================================

/api/Sucursal (GET, POST)
Response GET: SucursalDTO[]

SucursalDTO:
- sucursalId: int32
- nombre: string (max 100)
- estado: string (max 150)
- ciudad: string (max 150)
- direccion: string (max 150)
- esActivo, fechaIngreso: boolean/datetime
- usuarioSesionId: int32

================================================================================

/api/Usuario (GET, POST)
Response GET: UsuarioDTO[]

UsuarioDTO:
- usuarioId, perfilId: int32
- nombre, apellidoPaterno, loginName: string (max 20)
- correo: string (max 150)
- esActivo: boolean
- porcentajeComision: double
- esAdmin: boolean *(en DTO, no siempre en backend)*

Login explícito:
GET /api/usuario/{nombre},{contrasenia} → devuelve Usuario (no UsuarioDTO)
⚠️ Peligroso: contraseña en URL. Evitar en producción.

================================================================================

/api/UnidadMedida (GET, POST)
Response GET: UnidadMedidaDTO[]

UnidadMedidaDTO:
- unidadMedidaId: int32
- clave: string (max 30)
- nombre: string (max 100)
- esActivo, fechaIngreso, usuarioSesionId: boolean/datetime/int32

================================================================================

Endpoints de Exportación (todos POST):
- /api/Venta/export
- /api/Cliente/export
- /api/Sucursal/export
- /api/Producto/export
- /api/Proveedor/export
- /api/Compra/export
- /api/Inventario/export
- /api/Kardex/export
- /api/ReporteVentas/export

Todos usan query params igual que sus contrapartes GET y devuelven archivo binario.

================================================================================

Dashboard & Reportes:
--------------------------------------------------------------------------------
/api/Dashboard/GetInformacion → DashboardDTO[] (solo metadata básica)

/api/Dashboard/GetCobranzaCliente → DashboardDTO[]

/api/ReporteVentas → ReporteVentasDTO[]
Campos: nombreCliente, razonSocial, total (double)

/api/ReportePagos → ReportePagosDTO[]
⚠️ Swagger no define campos. Verificar backend.

================================================================================

CAMPOS COMUNES EN DTOs
--------------------------------------------------------------------------------
- esActivo: boolean ✅ Usado en casi todos los DTOs.
- fechaIngreso: datetime ✅ Formato ISO (yyyy-MM-ddTHH:mm:ss).
- usuarioSesionId: int32 ✅ ID de quien crea/modifica.

⚠️ INCONSISTENCIAS CRÍTICAS PARA REVISAR:
1. Nombres de IDs inconsistentes:
    - ciudadID (con D mayúscula) vs almacenId, ventaId, clienteId (camelCase estándar).
2. Fechas en todos los endpoints usan "date-time", asegurar formato ISO estricto.
3. EstatusVentaIds es string (no array), enviar como CSV: "1,2,3".
4. Login por path con contraseña: cambiar a POST con body.

================================================================================
FIN DE LA GUIA
