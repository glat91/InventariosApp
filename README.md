# Inventarios App - Android Kotlin Jetpack Compose

com.example.inventariosapp/
├── api/
│   └── ApiService.kt
├── di/
│   ├── NetworkModule.kt
│   └── RoomModel.kt
├── domain/
│   ├── model/
│   │   ├── client/
│   │   ├── error/
│   │   ├── login/
│   │   ├── payment/
│   │   ├── product/
│   │   ├── sales/
│   │   └── ClientModel.kt
│   ├── repository/
│   │   ├── client/
│   │   ├── login/
│   │   ├── payment/
│   │   ├── product/
│   │   └── sales/
│   ├── use_case/
│   │   ├── client/
│   │   ├── login/
│   │   ├── payment/
│   │   ├── product/
│   │   └── sales/
│   ├── AppModeSelead.kt
│   └── AppStateRepositoryImpl.kt
├── local/
│   ├── dao/
│   ├── entity/
│   ├── CompanyDatabase.kt
│   └── PriceListConverter.kt
├── navigation/
│   ├── Destinations.kt
│   └── SetupNavGraph.kt
├── session/
│   └── SessionManager.kt
├── ui/
│   ├── animations/
│   ├── component/
│   ├── dialog/
│   ├── theme/
│   └── view/
│       ├── BluetoothPrinterScreen/
│       ├── login/
│       ├── menu/
│       ├── new_sale/
│       ├── payment/
│       ├── products/
│       ├── sales/
│       ├── user_payments/
│       └── user_sales/
├── util/
│   ├── Constants.kt
│   ├── CustomEnums.kt
│   ├── Helpers.kt
│   └── NetworkMonitor.kt
├── BaseApplication.kt
├── BaseViewModel.kt
└── MainActivity.kt


## 🏗️ Arquitectura General

Esta es una aplicación móvil **Android** construida con:
- **Lenguaje:** Kotlin 100%
- **UI Framework:** Jetpack Compose (declarativo, moderno)
- **Arquitectura:** MVVM + Clean Architecture (capas claras de Separación de Responsabilidades)
- **Inyección de Dependencias:** Hilt
- **Persistencia Local:** Room Database
- **Red/HTTP:** Retrofit + OkHttp
- **Navigation:** Jetpack Navigation Compose

**Paquete raíz:** `com.example.inventariosapp`

---

## 📁 Estructura de Carpetas (Organizada por Dominio)

### `/api`
Contiene las interfaces y configuraciones para la comunicación con el servidor.

| Archivo | Descripción |
|---------|-------------|
| `ApiService.kt` | Interfaz Retrofit que define todos los endpoints REST (`@GET`, `@POST`, etc.) para login, ventas, productos, clientes, pagos. |

---

### `/di`
Contiene módulos de Hilt para la inyección de dependencias.

| Archivo | Descripción |
|---------|-------------|
| `NetworkModule.kt` | Configura OkHttpClient, Retrofit y ApiService como singletons. Define la `BASE_URL`. |
| `RoomModel.kt` | Configura la base de datos Room (`CompanyDatabase`) y los DAOs (Data Access Objects). |

---

### `/domain`
Núcleo del modelo de negocio. **No depende de Android**. Idealmente reutilizable.

| Subcarpeta | Descripción |
|------------|-------------|
| `model/` | Modelos de datos (data classes) que representan respuestas del servidor y entidades locales: <br>• `login`, `sales`, `client`, `payment`, `product`. Incluye modelos para errores (`ErrorModel`). |
| `repository/` | Implementaciones de las interfaces de repositorio. Lógica híbrida: **decide si obtener datos del servidor o de la base de datos local** (patrón de caché). <br>Ejemplo: `GetProductsRepositoryImp.kt`. |
| `use_case/` | Orquestadores que combinan repositorios para lograr una funcionalidad. Cada caso de uso es una clase con un único método `invoke()`. |
| `AppStateRepositoryImpl.kt`, `AppModeSelead.kt` | Archivos de gestión centralizada del estado y modo de la app (por revisar). |

---

### `/local`
Código relacionado con la persistencia de datos en dispositivo.

| Subcarpeta | Descripción |
|------------|-------------|
| `entity/` | Definición de tablas de Room (`@Entity`). Ejemplo: `ProductEntity`, `SalesEntity`. Incluye relaciones como `PostSaleWithProducts`. |
| `dao/` | Interfaz para operaciones CRUD sobre las entidades. <br>Ejemplo: `ProductDao.kt` (métodos: `getAllProducts()`, `insertAll()`). |
| `CompanyDatabase.kt` | Clase abstracta que define la base de datos (entidades, versiones, migraciones). |
| `PriceListConverter.kt` | TypeConverter para guardar listas complejas en Room. |

---

### `/session`
Lógica específica del ciclo de vida del usuario.

| Archivo | Descripción |
|---------|-------------|
| `SessionManager.kt` | Gestiona la sesión activa: inicio, validación (`isSessionValid()`), tiempo restante y cierre. Usa `SystemClock.elapsedRealtime()` para timers precisos. |

---

### `/ui`
Todo lo relacionado con la interfaz de usuario.

| Subcarpeta | Descripción |
|------------|-------------|
| `view/` | Pantallas principales (`View` + `ViewModel`). Cada pantalla tiene 2 archivos: <br>• `XXXScreen.kt`: Composable (UI) <br>• `XXXViewModel.kt`: Lógica de presentación, estado, llamadas a UseCases. <br>Ejemplos: `LoginScreen`, `ProductsScreen`, `NewSaleScreen`. |
| `component/` | Componentes reutilizables de UI: <br>• **Cards:** `CardSaleCmp`, `CardInventoryCmp`, etc. <br>• **Inputs:** `InputhWithTitleCmp`, `SearchBarCmp`. <br>• **Listas:** `ProductListCmp`, `ClientListCmp` (usando `LazyColumn`). |
| `dialog/` | Diálogos modales: `BasicDialogCmp`, `LoginDialogCmp`. |
| `theme/` | Configuración visual global: colores (`Color.kt`), tipografía (`Type.kt`), dimensiones. Usa un tema claro forzado (`InventariosAppTheme`). |
| `animations/` | Animaciones personalizadas para transiciones de pantallas y listas. |

---

### `/navigation`
Gestión del flujo de navegación.

| Archivo | Descripción |
|---------|-------------|
| `Destinations.kt` | Define los destinos únicos (rutas) como objetos (`LoginScreen`, `SalesScreen`). |
| `SetupNavGraph.kt` | Configura el `NavHost` y las rutas con transiciones animadas usando la librería `animatedComposable`. |

---

### `/util`
Utilidades del sistema y constantes.

| Archivo | Descripción |
|---------|-------------|
| `Constants.kt` | Clave de preferencias (SharedPreferences): credenciales, UUID del Bluetooth printer. <br>• `SINCRO_*`: Sincronización con servidor. |
| `Helpers.kt` | Funciones estáticas: formato de fechas, verificación de internet (`isInternetAvailable`). |
| `NetworkMonitor.kt` | Clase singleton que usa `ConnectivityManager.NetworkCallback` para detectar conexión/desconexión en tiempo real. |

---

## 🧠 Arquitectura de Datos

1. **UI (ViewModel)** llama a un UseCase.
2. UseCase invoca al repositorio correspondiente.
3. Repositorio decide:
   - Si hay internet → consulta servidor (`ApiService`) y guarda localmente.
   - Si no hay internet → lee directamente desde Room.
4. Los datos fluyen hacia la UI como `StateFlow` o `MutableState`.

> 📌 **Importante:** El repositorio es el encargado de la lógica de "cache" (fuentes múltiples).

---

## 🔐 Flujo de Sesion

1. Usuario ingresa credenciales → `LoginViewModel`.
2. Se llama a `ValitdateUserUseCase` → `ValidateUserRepositoryImp`.
3. Si las credenciales son válidas, se guarda la sesión en SharedPreferences usando `SessionManager.startSession()`.
4. Cada 1 segundo se verifica el tiempo restante (`observeRemainingTime()`). Al expirar, se cierra la sesión y muestra un diálogo.

---

## 📱 Pantallas Principales

| Pantalla | Descripción |
|----------|-------------|
| **Login** | Autenticación del usuario. |
| **Menu Principal (Lateral)** | Navegación global con menú deslizante. Muestra fechas de última sincronización (`lastUpdateClients`, `lastUpdateProducts`). |
| **Ventas (Sales)** | Visualiza las ventas en proceso y pendientes. Permite editar una venta seleccionada. |
| **Nueva Venta** | Flujo complejo: <br>• Selección de cliente (búsqueda). <br>• Selección de productos (búsqueda + inventario local). <br>• Ingreso de precios/cantidad. <br>• Guardado en modo online/offline. |
| **Productos** | Lista con búsqueda en tiempo real. Actualizable desde servidor. |
| **Pagos** | Visualización y gestión de pagos pendientes. |
| **BluetoothPrinterScreen** | Envío de comandas a impresoras Bluetooth (usando UUID estándar). |

---

## ⚙️ Configuración del Proyecto

| Clave | Valor |
|-------|-------|
| `compileSdk` | 36 |
| `minSdk` | 26 (Android 8.0) |
| `targetSdk` | 36 |
| Versión | `1.QA22` |

**Dependencias clave en `build.gradle.kts`:**
- Jetpack Compose BOM (versión dinámica)
- Hilt & Navigation Compose
- Room Runtime + KSP (compilación rápida)
- Retrofit, OkHttp Logging Interceptor

---

## 🧪 Buenas Prácticas Observadas

- ✅ **UI Descriptiva:** Uso intensivo de Jetpack Compose.
- ✅ **Separación clara:** Capas `domain`, `data` (local), `data` (remote).
- ✅ **Gestión de estado:** Uso de `StateFlow` y `MutableState`.
- ✅ **Reusabilidad:** Componentes UI en carpetas `component/`.
- ✅ **Offline-first:** El repositorio siempre tiene un fallback local.
- ✅ **Hilt para DI:** Inyección automática de dependencias sin código boilerplate.

---

## 🔧 Flujo de Desarrollo (Para IA)

1. **Aggiungi una nueva funcionalidad**:
   - Crea el modelo en `/domain/model`.
   - Agrega el método a `ApiService` (`/api`).
   - Implementa la lógica del repositorio (`/domain/repository`).
   - Crea un UseCase (`/domain/use_case`).
   - Crea la UI (screen, composable y ViewModel).

2. **Modifica una pantalla existente**:
   - Busca en `/ui/view/`.
   - El estado se maneja en `ViewModel.uiState: StateFlow<UiState>`.
   - Las llamadas a UseCase se hacen dentro de `viewModelScope.launch`.

3. **Cambia la base de datos local**:
   - Actualiza las entidades (`/local/entity`).
   - Ajusta el DAO correspondiente (`/local/dao`).
   - Incrementa la versión en `CompanyDatabase`.
</file>