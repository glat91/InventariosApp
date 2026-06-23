# Análisis del ViewModel y Pantalla de Nueva Venta

## Estructura del Proyecto

### NewSaleViewModel
El `NewSaleViewModel` es un componente que orquesta la lógica de negocio para crear o editar ventas. Utiliza múltiples use cases y repositorios para interactuar con:
1. **Servicios remotos (API)**: Para obtener productos, clientes y guardar ventas
2. **Base de datos local (Room)**: Como cache en modo offline
3. **Gestión de sesiones**: Validación de usuario activo

### Dependencias Clave

```kotlin
class NewSaleViewModel @Inject constructor(
    private val getSalesByIdUseCase: GetSalesByIdUseCase,
    private val editSaleUseCase: EditSaleUseCase,
    private val postSaleUseCase: PostSaleUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getInventarioProductoUseCase: GetInventarioProductoRepositoryImp,
    private val getInventarioUseCase: GetInventarioUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext private val cnx: android.content.Context
)
```

### Repositorios y Use Cases

| Clase | Responsabilidad | Fuentes de datos |
|-------|----------------|------------------|
| `GetSalesByIdRepositoryImp` | Obtener venta por ID | API remota (solo online) |
| `EditSaleRepositoryImp` | Editar venta existente | API remota (solo online) |
| `PostSaleRepositoryImp` | Guardar nueva venta | API remota y base de datos local (offline-first) |
| `GetProductsRepositoryImp` | Obtener productos | Base de datos local + API remota como fallback |
| `GetClientsRepositoryImp` | Obtener clientes | Base de datos local + API remota como fallback |

### Características de las Pruebas

1. **Pruebas Unitarias (Unit Tests)**: Deben mockear todos los repositorios y servicios externos
2. **Pruebas de Integración (Integration Tests)**: Deben usar una base de datos en memoria
3. **Pruebas UI (Espresso/Compose Tests)**: Deben probar la interacción con la pantalla

## Posibilidad de Automatización

### ✅ POSIBLE - Pruebas Unitarias
Las pruebas unitarias para los servicios del ViewModel son **totalmente automatizables** porque:
- Todos los repositorios son clases que implementan interfaces bien definidas
- Los use cases son simples delegadores a los repositorios
- No hay dependencias Android directas en la lógica de negocio

### ⚠️ DIFICULTAD - Pruebas UI Completas
Las pruebas UI para `NewSaleScreen` presentan desafíos:
- El ViewModel tiene dependencias Hilt complejas (BaseViewModel, SessionManager, NetworkMonitor)
- La pantalla usa `hiltViewModel()` que requiere inyección de dependencias
- Hay múltiples efectos colaterales (dialogos, navegación)

### ✅ POSIBLE - Pruebas UI Parciales
Se pueden crear pruebas UI automatizadas para:
- Verificar renderizado de componentes Compose
- Validar interacciones básicas (clicks, texto)
- Probar el flujo completo con mocks del ViewModel

## Recomendaciones para Implementación

### 1. Crear módulos de test Hilt
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    @Provides fun provideApiService(): ApiService = mockk()
    // ... otros mocks
}
```

### 2. Usar MockK para mocking
```kotlin
val repositoryImp = mockk<GetSalesByIdRepositoryImp>()
coEvery { repositoryImp.invoke(any(), any()) } returns Pair(mockResponse, null)
```

### 3. Probar repositorios individualmente
- Cada repositorio tiene lógica clara: "si hay internet usa API, si no usa DB local"
- Se puede mockear `NetworkMonitor.isConnected.value` para simular ambos escenarios

### 4. Pruebas de Use Cases
Los use cases son simples delegadores por lo que las pruebas serán básicas:
```kotlin
@Test
fun testGetSalesByIdUseCase_invokesRepository() = runTest {
    val result = getSalesByIdUseCase("123", true)
    verify { repositoryImp.invoke("123", true) }
}
```

### 5. Pruebas de Repositorios Complejas
Los repositorios tienen lógica más compleja:
- Manejo de errores (responses no exitosas, excepciones)
- Caché en base de datos local
- Sincronización bidireccional

Se recomienda usar una base de datos Room en memoria para pruebas de repositorio.

## Conclusión

**Las pruebas unitarias para los servicios usados por NewSaleViewModel son totalmente automatizables.**

La clave es:
1. Usar MockK para mockear dependencias externas
2. Probar cada capa (repository, use case) independientemente
3. Crear módulos de test Hilt con mocks
4. Para repositorios que usan Room, usar una base de datos en memoria

Las pruebas UI son más complejas pero se pueden automatizar parcialmente usando:
- `@HiltTestApplication` para inyección de dependencias
- Mock del ViewModel mediante `hiltViewModel()`
- Pruebas de Compose con `composeTestRule`
