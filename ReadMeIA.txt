# 📋 ReadMeIA - Análisis del Proyecto InventariosApp

## 🎯 ¿Qué hace este proyecto?

**InventariosApp** es una aplicación Android para gestión de inventarios, ventas, clientes y pagos diseñada para el negocio "Casa Jordan". Es una app tipo **punto de venta (POS)** con soporte **offline-first**, lo que significa que puede funcionar sin conexión a internet y sincronizar datos cuando se restablece la conexión.

### Funcionalidades principales:
1. **Login de usuarios** - Autenticación con sesión de 5000 horas (~208 días)
2. **Inventario de productos** - Consulta de productos con precios múltiples (precioVenta1-4) y stock
3. **Gestión de clientes** - Lista de clientes con información completa (datos fiscales, tipo de pago, crédito)
4. **Creación de ventas** - Punto de venta con selección de cliente, productos, cantidades y precios
5. **Historial de ventas** - Consulta de ventas por fecha con filtros
6. **Gestión de pagos** - Registro de pagos parciales/totales de ventas pendientes
7. **Ventas pendientes** - Sincronización de ventas guardadas offline al servidor
8. **Impresión Bluetooth** - Impresión de recibos mediante impresoras térmicas Bluetooth

---

## Arquitectura del Proyecto

La app sigue la arquitectura **Clean Architecture + MVVM** con las siguientes capas:

```
┌─────────────────────────────────────┐
│         UI Layer (Compose)          │  ← Screens, Views, UI State
│  (ui/view/ + ui/component/)         │
├─────────────────────────────────────┤
│         ViewModel Layer             │  ← ViewModel con StateFlow
│  (ui/view/*/ViewModel.kt)           │
├─────────────────────────────────────┤
│        Domain Layer                 │  ← Use Cases + Models
│  (domain/use_case/ + domain/model/) │
├─────────────────────────────────────┤
│       Data/Repository Layer         │  ← Repositories (Network + Local)
│  (domain/repository/)               │
├─────────────────────────────────────┤
│         Data Sources                │  ← Room DB + Retrofit API
│  (local/ + api/)                    │
└─────────────────────────────────────┘
```

---

## Estructura de Directorios Clave

### `app/src/main/java/com/example/inventariosapp/`

#### Entry Point
- **`MainActivity.kt`** - Actividad principal. Contiene variables globales estáticas (drawerState, scope, internetBtn, dialog states). Inicia el NavGraph y el menú lateral.
- **`BaseApplication.kt`** - Clase Application con @HiltAndroidApp para inyección de dependencias.

#### Navegación
- **`navigation/SetupNavGraph.kt`** - Configura todas las rutas de navegación con animaciones.
- **`navigation/Destinations.kt`** - Sealed class con todas las rutas: LoginScreen, SalesScreen, PaymentScreen, ProductsScreen, NewSaleScreen, PenndingSaleScreen, PrintScreen, PenndingPaymentsScreen.

#### Inyección de Dependencias (Hilt)
- **`di/RoomModel.kt`** - @Module que provee el CompanyDatabase y todos los DAOs (ClientDao, ProductDao, SalesDao, PayDao, PostSalesDao, InventoryDao, NewPayDao).
- **`di/NetworkModule.kt`** - @Module que configura Retrofit con OkHttp, logging interceptor y BASE_URL = `http://132.148.73.197/`

#### API REST
- **`api/ApiService.kt`** - Interfaz Retrofit con todos los endpoints:
  - Login: `GET Api/usuario/{user},{password}`
  - Productos: `GET Api/Producto`, `GET Api/Producto/GetByIdData`
  - Inventario: `GET Api/Inventario`
  - Clientes: `GET Api/Cliente`
  - Pagos: `GET/POST/DELETE Api/VentaPago`, `GET Api/TipoPago`
  - Ventas: `POST/GET/PUT Api/Venta`, `GET Api/VentaPago`

#### Base de Datos Local (Room)
- **`local/CompanyDatabase.kt`** - Database con 8 tablas (version 8):
  - ClientEntity, ProductEntity, SalesEntity, PayEntity
  - PostSaleEntity, PostSaleProductEntity, InventoryEntity, NewPayEntity
- **`local/dao/`** - DAOs: ClientDao, ProductDao, SalesDao, PayDao, PostSalesDao, InventoryDao, NewPayDao
- **`local/entity/`** - Entidades Room con @PrimaryKey y conversiones toModel()/toDb()
- **`local/PriceListConverter.kt`** - TypeConverter para listas de precios

#### Sesión
- **`session/SessionManager.kt`** - Maneja autenticación con DataStore. Sesión de 5000 horas. Guarda usuarioId, loginElapsed, perfilId, etc.

#### 📡 Utilidades
- **`util/NetworkMonitor.kt`** - Monitorea conexión a internet con NetworkCallback. Actualiza StateFlow isConnected.
- **`util/Constants.kt`** - Constantes: claves de persistencia, nombres de endpoints, UUID de impresora Bluetooth.
- **`util/Helpers.kt`** - Funciones auxiliares (fecha, persistencia, etc.)
- **`util/CustomEnums.kt`** - Enumeraciones personalizadas

#### BaseViewModel
- **`BaseViewModel.kt`** - ViewModel singleton inyectado con Hilt. Comparte funcionalidad entre todos los ViewModels:
  - Manejo de loader (showLoader/hideLoader)
  - Abrir/cerrar menú lateral (openMenu/closeMenu)
  - Monitor de internet
  - Gestión de sesión (startSession, logout, isSessionValid)
  - Timer de expiración de sesión

---

## Pantallas (Screens) y sus ViewModels

### 1. Login
- **`ui/view/login/LoginViewModel.kt`**
- **`ui/view/login/LoginScreen.kt`** / **`LoginView.kt`**
- Valida credenciales contra API. Si no hay internet, permite login offline.
- Guarda usuario/password si "remember me" está activo.
- Al validar: guarda datos en persistencia e inicia sesión.

### 2. Productos (Inventario)
- **`ui/view/products/ProductsViewModel.kt`**
- **`ui/view/products/ProductsScreen.kt`** / **`ProductsView.kt`**
- Carga productos desde API (online) o Room (offline).
- Búsqueda por descripción con filtro en tiempo real.
- Usa `GetProductsUseCase` → `GetProductsRepositoryImp`

### 3. Ventas (Historial)
- **`ui/view/sales/SalesViewModel.kt`**
- **`ui/view/sales/SalesScreen.kt`** / **`SalesView.kt`**
- Muestra ventas filtradas por rango de fechas.
- Busca por nombre de cliente.
- Usa `GetPendingSalesUseCase` con estatusVentaIds = "1,2"

### 4. Pagos
- **`ui/view/payment/PaymentsViewModel.kt`**
- **`ui/view/payment/PaymentsScreen.kt`** / **`PaymentsView.kt`**
- Muestra ventas pendientes de pago (estatusVentaIds = "2").
- Permite registrar pagos parciales/totales con `PostPaymentUseCase`.
- Permite eliminar pagos con `DeletePaymentUseCase`.
- **Impresión Bluetooth**: Conecta a impresora térmica via RFCOMM, imprime logo + texto del recibo.
- Búsqueda por nombre de cliente.

### 5. Nueva Venta
- **`ui/view/new_sale/NewSaleViewModel.kt`**
- **`ui/view/new_sale/NewSaleScreen.kt`** / **`NewSaleView.kt`**
- **La pantalla más compleja**. Permite:
  - Seleccionar cliente (búsqueda con filtro)
  - Agregar productos al carrito (diálogo con búsqueda)
  - Validar precios contra base de datos
  - Validar inventario disponible
  - Crear venta (POST a API o guardar offline en Room)
  - Editar venta existente (GET → modificar → PUT)
- Usa `PostSaleUseCase`, `GetSalesByIdUseCase`, `EditSaleUseCase`
- Carrito: `mutableStateListOf(SaleProductModel)` - lista observable de productos

### 6. Ventas Pendientes (Sincronización)
- **`ui/view/user_sales/PenndingSalesViewModel.kt`**
- **`ui/view/user_sales/PenndingSalesScreen.kt`** / **`PenndingSalesView.kt`**
- Muestra ventas guardadas offline (desde PostSalesDao).
- Botón "Subir ventas" que envía todas las ventas pendientes al servidor.
- Si hay internet: POST a API y limpia la tabla local.
- Si no hay internet: muestra mensaje de error.

### 7. Pagos Pendientes (Usuario)
- **`ui/view/user_payments/UserPaymentsViewModel.kt`**
- **`ui/view/user_payments/UserPaymentsScreen.kt`** / **`UserPaymentsView.kt`**
- Similar a Payments pero enfocado en pagos del usuario actual.

### 8. Impresora Bluetooth
- **`ui/view/BluetoothPrinterScreen/BluetoothPrinterScreen.kt`**
- **`ui/view/BluetoothPrinterScreen/PrintLogic.kt`**
- Escanea dispositivos Bluetooth emparejados.
- Conecta y envía datos de impresión.

---

## Flujo de Datos Offline-First

Este es el patrón central del proyecto:

```
¿Hay internet?
├── SÍ → Fetch desde API (Retrofit)
│        → Guardar en Room (cache)
│        → Retornar datos
└── NO → Leer desde Room (local)
        → Retornar datos
```

### Patrón en los Repositories:
Todos los repositories siguen este patrón:
```kotlin
suspend operator fun invoke(internetUse: Boolean): Pair<Data?, String?> {
    return if (networkMonitor.isConnected.value && internetUse) {
        fetchFromNetwork()  // API → Room → Data
    } else {
        fetchFromLocal()    // Room → Data
    }
}
```

### Sincronización de Ventas Offline:
1. Usuario crea venta sin internet → `PostSaleUseCase` guarda en `PostSalesDao`
2. Venta queda en tabla `PostSaleEntity` + `PostSaleProductEntity`
3. Cuando hay internet, va a "Ventas Pendientes" → botón "Subir ventas"
4. Se hace POST a API → si éxito, se borran las tablas locales

---

## 📊 Modelos de Datos Principales

### SalesModel (Venta)
```kotlin
ventaId, nombreCliente, folio, subtotal, descuento, iva, total,
montoPagado, montoPorPagar, fechaVenta, estatusVentaId, estatusVenta,
direccion, diasCredito, fechaLimitePago
```

### ProductsResponseModel (Producto)
```kotlin
productoId, codigo, departamento, descripcion, descripcionPresentacion,
estatus, esActivo, costo, precioVenta1, precioVenta2, precioVenta3,
precioVenta4, nombreUnidadMedida
```

### ClientResponseModel (Cliente)
```kotlin
clienteId, tipoPersonaId, tipoGiroId, tipoPagoId, nombreCliente,
razonSocial, rfc, curp, direccion, telefono, correo, montoCredito,
diasCredito, esActivo, estatus
```

### PayModel (Pago)
```kotlin
ventaPagoId, ventaId, montoPago, fecha, observaciones, origenId,
tipoConexionId, nombreVendedor, usuarioSesionId
```

---

## Puntos Clave para Modificar

### Para agregar un nuevo endpoint API:
1. Agregar método en `api/ApiService.kt`
2. Crear modelo de respuesta en `domain/model/`
3. Crear Repository en `domain/repository/`
4. Crear UseCase en `domain/use_case/`
5. Inyectar en ViewModel correspondiente

### Para agregar una nueva pantalla:
1. Agregar ruta en `navigation/Destinations.kt`
2. Crear extensión en `navigation/SetupNavGraph.kt` (animatedComposable)
3. Crear Screen.kt, View.kt, ViewModel.kt en `ui/view/nueva_pantalla/`
4. Agregar navegación desde el menú lateral (`LateralMenuCmp.kt`)

### Para modificar lógica de negocio:
- **UI changes** → `ui/view/*/Screen.kt` y `View.kt`
- **State/Logic changes** → `ui/view/*/ViewModel.kt`
- **Business rules** → `domain/use_case/*/*UseCase.kt`
- **Data fetching** → `domain/repository/*/*RepositoryImp.kt`

### Variables globales importantes (MainActivity.kt):
- `MainActivity.internetBtn` - Estado manual de internet (toggle)
- `MainActivity.mainDialog` - Control del dialog global
- `MainActivity.mainDialogTitle`, `mainDialogMsg`, `mainDialogColor` - Contenido del dialog
- `MainActivity.startDate`, `endDate` - Rango de fechas para filtros
- `MainActivity.lastUpdateClient/Products/Sells/Inventory` - Timestamps de actualización
- `MainActivity.currentRoute` - Ruta actual para UI

### SessionManager:
- Duración de sesión: `5000 * 60 * 60 * 1000L` (~5000 horas)
- Claves de persistencia en `Constants.kt`
- `isSessionValid()` verifica elapsed time desde login

---

## Tecnologías y Dependencias

- **UI**: Jetpack Compose, Material3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger)
- **Navigation**: Jetpack Navigation Compose
- **Local DB**: Room (con KSP)
- **Networking**: Retrofit + OkHttp + Gson
- **State Management**: StateFlow + mutableStateOf
- **Testing**: JUnit, MockK, Compose Testing
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36
- **Kotlin**: 17

---

## Convenciones del Código

- **Naming**: Repositories terminan en `RepositoryImp`, UseCases en `UseCase`
- **Return types**: Pair<Data?, String?> para (datos, error)
- **UI State**: MutableStateFlow con data class UiState
- **Entity conversions**: Funciones de extensión `toDb()`, `toModel()`, `toEntity()`
- **Dialogs**: Se usa `MainActivity.mainDialog` como dialog global único
- **Loaders**: `baseViewModel.showLoader()` / `hideLoader()`

---

## Notas Importantes

1. **BASE_URL** está hardcodeada en `NetworkModule.kt`: `http://132.148.73.197/`
2. **Bluetooth printing** requiere permisos BLUETOOTH_CONNECT y BLUETOOTH_SCAN (Android 12+)
3. **Precio de ventas** usa 4 niveles de precio (precioVenta1-4)
4. **Estatus de ventas**: "1,2" = pendientes, otros valores = completadas/canceladas
5. **tipoConexionId**: 1 = online, 2 = offline
6. **origenId**: Siempre 2 para ventas/pagos
7. La app usa `fallbackToDestructiveMigration()` en Room (pierde datos en migraciones)
8. **MainActivity** tiene muchas variables companion object estáticas - esto es un code smell pero es el patrón actual

---

## Tests

Los tests están en `app/src/test/java/com/example/inventariosapp/`:
- `domain/use_case/` - Tests de UseCases
- `domain/repository/` - Tests de Repositories
- `ui/view/` - Tests de Screens y ViewModels
- `TestModules.kt` - Módulos de test para Hilt

---

*Última actualización del análisis: Basado en código actual del proyecto 09/08/2026*