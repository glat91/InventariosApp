# Propuesta de Cambios: Entidades Locales ↔ Swagger (Inventario API)

**Última actualización**: 2025-04-05  
**Fuente de verdad**: `swagger.md` (v2.0)  
**Objetivo**: Homologar entidades Room (`*.kt`) con DTOs definidos en Swagger

---

## 📋 Resumen Ejecutivo

| Entidad | Estado | Cambios Requeridos |
|---------|--------|-------------------|
| `ClientEntity` | ✅ Alineado | Ninguno |
| `ProductEntity` | ⚠️ **Crítico** | 1 campo obligatorio faltante (`unidadMedidaId`) y posibles ajustes de nombre |
| `SalesEntity` | ❌ **Crítico** | 4 campos obligatorios faltantes (`clienteId`, `retencion`, `usuarioSesionId`, `esActivo`) |
| `InventoryEntity` | ⚠️ Pendiente | No hay DTO definido en Swagger (verificar backend) |
| `PayEntity` | ✅ Alineado (con observación) | Campo `usuarioSesionId` ya existe, pero se debe verificar consistencia |
| `NewPayEntity` | ❌ **Crítico** | Falta campo obligatorio (`usuarioSesionId` con tipo `Int?`) |
| `PostSaleEntity` | ✅ Alineado | Ya cubre todos los campos obligatorios |

---

## 📚 DTOs Requeridos por Swagger vs Entidades Actuales

### 1. ClienteDTO → ClientEntity
**Swagger:**
```yaml
clienteId: int32
tipoPersonaId, tipoGiroId, tipoPagoId: int32
esActivo: boolean
fechaIngreso: datetime (ISO)
usuarioSesionId: int32
direccion: string (max 250)
calle: string (max 200)
ciudadId, estadoId, paisId: int32
codigoPostal: string (max 25)
```

**ClientEntity actual:**
```kotlin
clienteId ✓
tipoPersonaId, tipoGiroId, tipoPagoId ✓
esActivo ✓
fechaIngreso ✓
usuarioSesionId ✓
direccion ✓
calle ✓
ciudadId, estadoId, paisId ✓
codigoPostal ✓
// + campos extra (rfc, curp, nombreCliente, etc.)
```

**✅ Conclusión**: Alineado. **No se requieren cambios.**

---

### 2. ProductoDTO → ProductEntity  
**Swagger:**
```yaml
productoId: int32
descripcion: string (max 300)
presentacion: string (max 100)       ← ⚠️ FALTA EN ENTIDAD ACTUAL
unidadMedidaId: int32                ← ❌ CRÍTICO: CAMPO OBLIGATORIO FALTANTE
costo: double
precioVenta1: double
esActivo, fechaIngreso, usuarioSesionId: boolean/datetime/int32
```

**ProductEntity actual (faltantes):**
| Campo Swagger      | En Entidad? | Tipo Actual | Acción Requerida |
|--------------------|-------------|-------------|------------------|
| `presentacion`     | ❌ No       | —           | Agregar como `String?` o reasignar desde `descripcionPresentacion` |
| `unidadMedidaId`   | ❌ **FALTA**| —           | **Agregar obligatoriamente** como `Int?` |

#### 🔧 Propuesta de Modificación para ProductEntity

```kotlin
// A continuación del @PrimaryKey y productoId:
@ColumnInfo("unidadMedidaId") val unidadMedidaId: Int? = null,
```

Opcional (si se desea coincidencia exacta con Swagger):
```kotlin
@ColumnInfo("presentacion") val presentacion: String? = null,  // si no coincide con descripcionPresentacion
// o renombrar:
@ColumnInfo("presentacion") val descripcionPresentacion: String? = null,
```

> **Nota**: Si el backend devuelve `unidadMedidaId` pero tú solo usas `nombreUnidadMedida`, considera mantener ambos para compatibilidad futura.

---

### 3. VentaDTO → SalesEntity  
**Swagger:**
```yaml
ventaId: int32
clienteId, estatusVentaId: int32
subtotal, descuento, iva, retencion, total: double
fechaVenta: datetime (ISO)
usuarioSesionId: int32
esActivo: boolean
```

**SalesEntity actual (faltantes):**
| Campo Swagger     | En Entidad? | Tipo Actual       | Acción Requerida |
|-------------------|-------------|-------------------|------------------|
| `clienteId`       | ❌ **FALTA**| —                 | Agregar como `Int?` |
| `retencion`       | ❌ **FALTA**| —                 | Agregar como `Double?` (¡es obligatorio!) |
| `usuarioSesionId` | ❌ No está  | —                 | Agregar como `Int?` |
| `esActivo`        | ❌ No está  | —                 | Agregar como `Boolean?` |

#### 🔧 Propuesta de Modificación para SalesEntity

```kotlin
// Añadir después de @PrimaryKey:
var clienteId: Int? = null,          // obligatorio según Swagger
var retencion: Double? = null,       // obligatorio (¡falta!)
var usuarioSesionId: Int? = null,    // obligatorio
var esActivo: Boolean? = null        // obligatorio
```

> **⚠️ Crítico**: `retencion` está explícitamente en Swagger pero faltaba incluso en `PostSaleEntity`. Asegúrate de incluirla aquí también.

---

### 4. InventoryEntity → (No definido en Swagger)

**Swagger no tiene definición de `InventarioDTO`, pero menciona:**
- `/api/Inventario/export` endpoint

**InventoryEntity actual (campos existentes):**
```kotlin
productoId, producto, almacen, unidadMedida, total,
stockMaximo, stockMinimo, msgStockMaximo, msgStockMinimo,
msgCaducidad, porCaducar
```

**⚠️ Acción requerida:**
- Consultar al backend si el endpoint `/api/Inventario` devuelve campos específicos.
- Si no hay documentación formal, mantener así o eliminar si no se usa.

---

### 5. VentaPagoDTO → PayEntity  
**Swagger:**
```yaml
ventaPagoId, ventaId: int32
montoPago: double
fecha: datetime
usuarioSesionId: int32   ← ✅ YA ESTÁ EN ENTIDAD ACTUAL
```

**PayEntity actual:**
```kotlin
ventaPagoId ✓ (como @PrimaryKey)
ventaId ✓
montoPago ✓
fecha ✓
usuarioSesionId ✓ (ya existe)
// + campos adicionales: observaciones, origenId, tipoConexionId, fechaIngreso, esActivo, etc.
```

**✅ Conclusión**: Alineado. **No se requieren cambios**, pero verificar que `usuarioSesionId` sea no nulo (`Int?`) en lugar de `Int`.

---

### 6. VentaPagoDTO → NewPayEntity  
**Swagger (misma definición que PayEntity):**
```yaml
ventaPagoId, ventaId: int32
montoPago: double
fecha: datetime
usuarioSesionId: int32
```

**NewPayEntity actual:**
```kotlin
// No tiene @ColumnInfo("usuarioSesionId") ni anotación Room
var usuarioSesionId: Int = 0   // ❌ No coincide con tipo swagger (Int vs Int?)
```

#### 🔧 Propuesta de Modificación para NewPayEntity

Cambiar:
```kotlin
var usuarioSesionId: Int = 0,
```
por:
```kotlin
@ColumnInfo("usuarioSesionId") var usuarioSesionId: Int? = null,
// o si prefieres mantener default:
@ColumnInfo("usuarioSesionId") var usuarioSesionId: Int = 1, // valor temporal (ej. usuario anónimo)
```

> **⚠️ Importante**: Si `NewPayEntity` es solo para sincronización local (off-line), puedes usar un valor por defecto (`0` o `1`). Pero si se envía al backend, debe coincidir con el tipo del DTO.

---

### 7. VentaDTO → PostSaleEntity  
**Swagger (misma definición que arriba):**
```yaml
ventaId, clienteId, estatusVentaId, subtotal, descuento, 
iva, retencion, total: double
fechaVenta: datetime
usuarioSesionId, esActivo: boolean/datetime/int32
```

**PostSaleEntity actual (verificado en lectura):**
```kotlin
ventaId ✓
clienteId ✓ (var clienteId: Int? = null)
estatusVentaId ✓ (var estatusVentaId: Int = 1)
subtotal, descuento, iva, retencion, total ✓ (todos Double?)
fechaVenta ✓ (String? con formato ISO)
usuarioSesionId ✓ (Int? = null)
esActivo ✓ (Boolean = true)
```

✅ **Conclusión**: Ya cumple todos los requisitos. **No se requieren cambios.**

---

## 🧩 Resumen de Cambios por Archivo

### ✅ Archivos que NO necesitan cambios
| Archivo | Razón |
|---------|-------|
| `ClientEntity.kt` | Alineado con ClienteDTO |
| `PayEntity.kt` | Alineado (verificado usuarioSesionId presente) |
| `PostSaleEntity.kt` | Alineado con VentaDTO |

### 📝 Archivos que SÍ necesitan cambios

#### 1. `ProductEntity.kt`
```kotlin
// Agregar campo faltante:
@ColumnInfo("unidadMedidaId") val unidadMedidaId: Int? = null,
```

**Razón**: Swagger lo marca como obligatorio.

---

#### 2. `SalesEntity.kt`
```kotlin
// Añadir después de @PrimaryKey:
var clienteId: Int? = null,          // obligatorio
var retencion: Double? = null,       // obligatorio (¡falta!)
var usuarioSesionId: Int? = null,    // obligatorio  
var esActivo: Boolean? = null        // obligatorio
```

**Razón**: Swagger define 4 campos obligatorios que faltan.

---

#### 3. `NewPayEntity.kt`
```kotlin
// Opción A (si se manda al backend):
@ColumnInfo("usuarioSesionId") var usuarioSesionId: Int? = null,

// Opción B (si es solo local/offline, con default):
@ColumnInfo("usuarioSesionId") var usuarioSesionId: Int = 1,
```

**Razón**: Swagger lo exige como obligatorio (`int32`).

---

## 🔎 Pasos Sugeridos para Implementación

1. **Revisar y aplicar cambios por entidad**, una a la vez:
   - Iniciar con `ProductEntity`
   - Verificar que el código compile
   - Ejecutar pruebas locales (si existen)
   - Continuar con `SalesEntity`
   - Y así sucesivamente...

2. **Validar mapeos** (`toModel()`, `toDB()`) después de cada cambio.

3. **actualizar la base de datos Room**:  
   Si los cambios afectan el esquema (nuevos campos), incrementar versión en `RoomDatabase` y añadir migraciones.

4. **Probar sincronización con backend** para verificar que no falle al mapear campos faltantes.

---

## 📌 Notas Adicionales

- Swagger menciona inconsistencias como `ciudadID` (con "D" mayúscula); si Android usa camelCase estándar (`ciudadId`), mantenerlo así.
- Todos los DTOs usan formato ISO para fechas (`yyyy-MM-ddTHH:mm:ss`). Asegurar que las cadenas en Room sigan este formato.
- En endpoints con `EstatusVentaIds`, el Swagger espera un string CSV (`"1,2,3"`). Si tu app usa listas locales, convertir al enviar.

---

## ✅ Checklist Final

- [ ] `ProductEntity`: Agregar `unidadMedidaId: Int?`
- [ ] `SalesEntity`: Agregar `clienteId`, `retencion`, `usuarioSesionId`, `esActivo`
- [ ] `NewPayEntity`: Corregir tipo de `usuarioSesionId` (`Int?` o `Int` con default)
- [ ] Revisar y compilar cada cambio antes de pasar al siguiente archivo
- [ ] Actualizar migraciones Room si se modifica esquema
- [ ] Probar sincronización con Swagger-compliant backend

---

**Fin del documento**
