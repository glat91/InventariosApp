# Guía de Pruebas para NewSaleViewModel y NewSaleScreen

## Estructura de Pruebas Creadas

### 1. Pruebas Unitarias de Use Cases (app/src/test/java/com/example/inventariosapp/domain/use_case/)

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `GetSalesByIdUseCaseTest.kt` | Prueba del use case para obtener venta por ID | ✅ Creado |
| `EditSaleUseCaseTest.kt` | Prueba del use case para editar ventas | ✅ Creado |
| `PostSaleUseCaseTest.kt` | Prueba del use case para guardar nuevas ventas | ✅ Creado |
| `GetProductsUseCaseTest.kt` | Prueba del use case para obtener productos | ✅ Creado |
| `GetClientsUseCaseTest.kt` | Prueba del use case para obtener clientes | ✅ Creado |
| `GetInventarioUseCaseTest.kt` | Prueba del use case para obtener inventario | ✅ Creado |

### 2. Pruebas de Repositorios (app/src/test/java/com/example/inventariosapp/domain/repository/)

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `GetSalesByIdRepositoryImpTest.kt` | Prueba del repositorio con mockeo de API | ✅ Actualizado - Uso de Retrofit Response |
| `EditSaleRepositoryImpTest.kt` | Prueba del repositorio con validación de sesión | ✅ Creado |
| `PostSaleRepositoryImpTest.kt` | Prueba del repositorio con base de datos local | ✅ Creado |
| `GetProductsRepositoryImpTest.kt` | Prueba del repositorio con fallback a DB local | ✅ Creado |
| `GetClientsRepositoryImpTest.kt` | Prueba del repositorio con fallback a DB local | ✅ Creado |
| `GetInventarioProductoRepositoryImpTest.kt` | Prueba del repositorio para obtener inventario por producto | ✅ Actualizado - Uso de Gson serialización |

### 3. Pruebas del ViewModel (app/src/test/java/com/example/inventariosapp/ui/view/new_sale/)

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `NewSaleViewModelTest.kt` | Pruebas unitarias para NewSaleViewModel | ✅ Creado |

### 4. Pruebas UI (app/src/test/java/com/example/inventariosapp/ui/view/new_sale/)

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `NewSaleScreenTest.kt` | Pruebas UI para NewSaleScreen con Compose Testing APIs | ✅ Actualizado - Uso de Compose Rule correcta |

### 5. Módulos de Test (app/src/test/java/com/example/inventariosapp/)

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `TestModules.kt` | Módulos Hilt para pruebas con mocks y base de datos en memoria | ✅ Creado |
| `ANALISIS_NEW_SALE.md` | Análisis detallado del ViewModel y estrategia de pruebas | ✅ Creado |

---

## Correcciones Recientes (2024)

### GetInventarioProductoRepositoryImpTest.kt ✅
**Problema**: Error con `Response.Builder()` y tipo Response (okhttp3 vs retrofit2)
- **Solución**: Cambiar a `Response.success(mockResponse)` y `Response.error(500, "".toResponseBody())`

### GetSalesByIdRepositoryImpTest.kt ✅  
**Problema**: Igual que arriba, + error en test con @Test(expected = IOException::class)
- **Solución**: 
  - Cambiar a `Response.success()` y `Response.error()`
  - Mover `runBlocking` dentro del método para que la firma sea compatible

### GetInventarioUseCaseTest.kt ✅
**Problema**: Error de tipo en respuesta con error (String vs ErrorModel)
- **Solución**: Usar `ErrorModel(MsgErrorModel(rawValue = errorMsg))`

### NewSaleScreenTest.kt ✅
**Problema**: Referencia a clase inexistente `AndroidComposeTestCase`
- **Solución**: 
  - Cambiar a `createAndroidComposeRule<MainActivity>()`
  - Usar `composeTestRule.activity` en lugar de `ruleActivity`

---

## Ejecución de Pruebas

### 1. Pruebas Unitarias (JVM)
```bash
./gradlew testDebugUnitTest
```

### 2. Pruebas Instrumentadas (Android)
```bash
./gradlew connectedAndroidTest
```

### 3. Ver Resultados
- Pruebas JVM: `app/build/reports/tests/testDebugUnitTest/index.html`
- Pruebas instrumentadas: Reporte en consola

## Dependencias Faltantes para Mocking

Para ejecutar las pruebas que usan `mockk`, se necesitan agregar las siguientes dependencias a `gradle/libs.versions.toml`:

```toml
mockk = "1.13.5"
mockk-android = "1.13.5"
androidx-test-ext-junit = "1.2.1"
espresso-core = "3.6.1"
```

Y en `app/build.gradle.kts`:

```kotlin
testImplementation(libs.mockk)
testImplementation(libs.mockk.android)
androidTestImplementation(libs.androidx.test.ext.junit)
androidTestImplementation(libs.espresso.core)
```

## Estrategia de Pruebas

### 1. Use Cases
- Simples delegadores a repositorios
- Probar invocación correcta con argumentos específicos
- Validar respuestas exitosas y errores

### 2. Repositorios
- **Con base de datos**: Usar Room en memoria (`Room.inMemoryDatabaseBuilder`)
- **Con APIs externas**: Usar MockK para mockear responses
- Probar ambos flujos: online (API) y offline (DB local)

### 3. ViewModel
- Mockear todos los use cases y dependencias externas
- Probar:
  - Inicialización correcta del estado
  - Llamadas a use cases con argumentos correctos
  - Actualización de estado (`_uiState`)
  - Manejo de errores
  - Validaciones (ej: `validateProductsWithDb`)

### 4. Pantallas UI (Compose)
- Probar renderizado de componentes
- Validar interacciones básicas (clicks, texto)
- Usar `@HiltTestApplication` para inyección de dependencias

## Casos de Prueba No Automatizables

### ❌ NO AUTOMATIZABLE - Dialogos del sistema
```kotlin
// Estas llamadas requieren interacción humana o mocks complejos:
MainActivity.mainDialogMsg.value = "..."
MainActivity.mainDialog.value = true
```

### ⚠️ DIFICIL - Validaciones de UI complejas
- Interacciones múltiples en secuencia
- Animaciones de transición
- Validación visual precisa

### ✅ AUTOMATIZABLE - Lógica de negocio
- Todos los métodos del ViewModel que no interactúan directamente con UI
- Repositorios que manejan lógica de cache
- Use cases que delegan a repositorios

## Recomendaciones

1. **Comenzar con pruebas unitarias simples**: Ejecutar las pruebas de use cases primero
2. **Agregar dependencias de mocking**: Agregar MockK para poder ejecutar todas las pruebas
3. **Pruebas de integración**: Usar base de datos en memoria para probar repositorios completos
4. **Pruebas UI parciales**: Probar componentes Compose individuales sin depender del ViewModel completo

## Conclusión

Las pruebas para `NewSaleViewModel` y `NewSaleScreen` son **parcialmente automatizables**:

- ✅ **100% automatizable**: Lógica de negocio (use cases, repositorios)
- ⚠️ **Parcialmente automatizable**: ViewModel (con mocks adecuados)
- ❌ **No automatizable**: Diálogos del sistema y efectos colaterales externos

El código actual tiene buenas prácticas de arquitectura (MVVM + Clean Architecture) que facilitan la testing. Las pruebas unitarias pueden ejecutarse inmediatamente una vez se agreguen las dependencias de mocking (MockK).
