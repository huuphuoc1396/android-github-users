# CLAUDE.md
# Android project architecture rules. Read this before writing or modifying any code.

---

## PROJECT

- Package: `com.example.app`
- UI: Jetpack Compose (no Fragments, no XML layouts)
- DI: Hilt
- Async: Coroutines + Flow
- Network: Retrofit + OkHttp + Gson
- Local DB: Room
- Local KV: DataStore
- Image loading: Coil

---

## MODULE MAP

```
:app
core/
  common/       ← shared Kotlin contracts, no Android framework, no Hilt
  network/      ← Retrofit, OkHttpClient, safeApiCall — Hilt modules live here
  database/     ← AppDatabase, all DAOs, migrations — Hilt modules live here
  ui/           ← AppTheme, shared composables, design tokens
  navigation/   ← Route, AppNavigator, NavigationIntent
  testing/      ← MainDispatcherRule, fakes, test factories
feature/
  <name>/       ← one module per feature (e.g. feature/auth, feature/home)
  <name>/
    api/        ← optional: public contract interface, used by other features
    impl/       ← optional: implementation of the contract, only :app imports this
```

---

## OWNERSHIP — where each piece of code lives

| Code | Module |
|---|---|
| OkHttpClient, Retrofit, GsonConverterFactory | `core:network` — `NetworkModule` |
| AppDatabase (abstract), all DAO interfaces | `core:database` — `DatabaseModule` |
| CoroutineDispatcher + `@Dispatcher` qualifier | `core:common` |
| `safeApiCall`, `ApiException` | `core:network` |
| `SessionManager` interface, `UserSession` | `core:common` |
| `AppEventBus`, `AppEvent` sealed interface | `core:common` |
| Kotlin extensions, `Result` helpers | `core:common` |
| `AppTheme`, `ColorScheme`, `Typography` | `core:ui` |
| Shared composables (`AppButton`, `AppTextField`, `LoadingScreen`, `ErrorScreen`) | `core:ui` |
| `Route` sealed class (all destinations) | `core:navigation` |
| `AppNavigator` interface, `NavigationIntent` | `core:navigation` |
| `AppNavigatorImpl` | `:app` |
| `NavigationEffects` composable | `core:navigation` |
| `MainDispatcherRule` | `core:testing` |
| Fake implementations (`FakeSessionManager`, `FakeAppNavigator`, etc.) | `core:testing` |
| Repository interface | `feature:x/domain/repository/` |
| UseCase classes | `feature:x/domain/usecase/` |
| Domain models (not DTOs) | `feature:x/domain/model/` |
| Repository implementation | `feature:x/data/repository/` |
| Retrofit API interface | `feature:x/data/remote/` |
| Response DTOs | `feature:x/data/remote/dto/` |
| DTO → domain mappers | `feature:x/data/mapper/` |
| Room DAO implementation | `feature:x/data/local/` |
| DataStore wrapper | `feature:x/data/local/` |
| `@Binds` repo, `@Provides` API/DAO, `@IntoSet` interceptor | `feature:x/di/XxxModule.kt` |
| `NavGraphBuilder` extension | `feature:x/navigation/XxxNavGraph.kt` |
| Stateful screen composable | `feature:x/presentation/xxx/XxxScreen.kt` |
| UiState sealed interface | `feature:x/presentation/xxx/XxxUiState.kt` |
| `@HiltViewModel` | `feature:x/presentation/xxx/XxxViewModel.kt` |
| Feature API contract interface | `feature:x:api` |
| Feature API contract implementation | `feature:x:impl` |
| `@Binds` contract impl → interface | `:app/di/AppBindingModule` |

---

## DEPENDENCY DIRECTION

```
:app → feature:* → core:*
:app → core:*
feature:x → feature:y:api     ← ALLOWED (public contract only)
feature:x → feature:y         ← FORBIDDEN
feature:x → feature:y:impl    ← FORBIDDEN
core:*    → feature:*         ← FORBIDDEN
```

---

## HARD RULES

### Module boundaries
- `feature:x` NEVER imports `feature:y` internal packages
- `core:*` NEVER imports any `feature:*`
- Only `:app` imports `feature:x:impl` modules
- `core:network` and `core:database` own their own Hilt `@Module` — no separate `core:di`

### DI
- OkHttpClient is extended via `@IntoSet Interceptor` contributed by each feature — `NetworkModule` is never modified
- Each feature provides its own DAO: `db.xDao()` inside its own `di/` module
- Each feature creates its own Retrofit service: `retrofit.create(XxxApi::class.java)` inside its own `di/` module
- `@Binds` requires `abstract class`; `@Provides` requires `object` or `companion object`

### Compose
- ViewModel NEVER holds a reference to `NavController`
- Navigation from ViewModel: call `navigator.navigate(NavigationIntent)` only
- Every screen = one **stateful** composable (`hiltViewModel`) + one **private stateless** `XxxContent` composable
- `UiState` MUST be a sealed interface — NEVER use boolean flag combinations (`isLoading + isError`)
- Stateless `XxxContent` must be previewable with no ViewModel dependency
- Shared composables live in `core:ui` — never duplicated inside a feature

### Gson
- DTOs use `@SerializedName` annotations — domain models never use Gson annotations
- DTO → domain conversion happens in `feature:x/data/mapper/XxxMappers.kt` only

### Images
- All image loading uses Coil (`AsyncImage` or `rememberAsyncImagePainter`) — never Glide or Picasso
- Image URL parameters are passed as `String` from ViewModel to composable

---

## NAMING CONVENTIONS

| What | Convention |
|---|---|
| Stateful screen | `XxxScreen` |
| Stateless content | `XxxContent` (private) |
| UiState | `XxxUiState` (sealed interface: `Idle`, `Loading`, `Success`, `Error`) |
| ViewModel | `XxxViewModel` |
| UseCase | `VerbNounUseCase` — e.g. `LoginUseCase`, `PlaceOrderUseCase` |
| Repository interface | `XxxRepository` |
| Repository impl | `XxxRepositoryImpl` |
| DTO | `XxxResponse`, `XxxRequest` |
| Domain model | `Xxx` (no suffix) |
| Mapper file | `XxxMappers.kt` (top-level extension functions) |
| DI module | `XxxModule` |
| NavGraph extension | `fun NavGraphBuilder.xxxNavGraph(...)` |

---

## CROSS-FEATURE INTERACTION

| Situation | Pattern |
|---|---|
| Navigate to another screen | `navigator.navigate(NavigationIntent.NavigateTo(Route.X.path))` |
| Read shared data (e.g. current user) | Inject `SessionManager` from `core:common` |
| Broadcast event to multiple features | `eventBus.publish(AppEvent.XxxHappened(...))` |
| Call logic owned by another feature | Inject contract interface from `feature:x:api` |

---

## CANONICAL FEATURE STRUCTURE

```
feature/auth/
├── build.gradle.kts
└── src/main/kotlin/com/example/app/feature/auth/
    ├── navigation/
    │   └── AuthNavGraph.kt
    ├── presentation/
    │   └── login/
    │       ├── LoginScreen.kt
    │       ├── LoginViewModel.kt
    │       └── LoginUiState.kt
    ├── domain/
    │   ├── usecase/
    │   │   └── LoginUseCase.kt
    │   ├── model/
    │   │   └── AuthToken.kt
    │   └── repository/
    │       └── AuthRepository.kt
    ├── data/
    │   ├── repository/
    │   │   └── AuthRepositoryImpl.kt
    │   ├── remote/
    │   │   ├── AuthApi.kt
    │   │   └── dto/
    │   │       ├── LoginRequest.kt
    │   │       └── AuthResponse.kt
    │   ├── local/
    │   │   └── TokenDataStore.kt
    │   └── mapper/
    │       └── AuthMappers.kt
    └── di/
        └── AuthModule.kt
```

---

## CODE TEMPLATES

### UiState
```kotlin
sealed interface XxxUiState {
    object Idle    : XxxUiState
    object Loading : XxxUiState
    data class Success(val data: T)      : XxxUiState
    data class Error(val message: String): XxxUiState
}
```

### ViewModel
```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    private val useCase: XxxUseCase,
    private val navigator: AppNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow<XxxUiState>(XxxUiState.Idle)
    val uiState: StateFlow<XxxUiState> = _uiState.asStateFlow()
}
```

### Screen split
```kotlin
@Composable
fun XxxScreen(viewModel: XxxViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    XxxContent(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
private fun XxxContent(uiState: XxxUiState, onAction: () -> Unit) {
    when (uiState) {
        is XxxUiState.Idle    -> Unit
        is XxxUiState.Loading -> LoadingScreen()
        is XxxUiState.Success -> { /* render uiState.data */ }
        is XxxUiState.Error   -> ErrorScreen(message = uiState.message)
    }
}
```

### Feature DI module
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class XxxModule {

    @Binds @Singleton
    abstract fun bindXxxRepository(impl: XxxRepositoryImpl): XxxRepository

    companion object {
        @Provides @Singleton
        fun provideXxxApi(retrofit: Retrofit): XxxApi =
            retrofit.create(XxxApi::class.java)

        @Provides
        fun provideXxxDao(db: AppDatabase): XxxDao = db.xxxDao()

        // Include only if this feature needs to intercept HTTP requests
        @Provides @IntoSet @Singleton
        fun provideXxxInterceptor(...): Interceptor = XxxInterceptor(...)
    }
}
```

### NavGraph extension
```kotlin
fun NavGraphBuilder.xxxNavGraph(navController: NavHostController) {
    composable(Route.Xxx.path) { XxxScreen() }
}
```

### DTO → domain mapper
```kotlin
// feature:x/data/mapper/XxxMappers.kt
fun XxxResponse.toDomain(): Xxx = Xxx(
    id = this.id,
    name = this.name
)
```

### Repository impl
```kotlin
class XxxRepositoryImpl @Inject constructor(
    private val api: XxxApi,
    private val dao: XxxDao
) : XxxRepository {
    override suspend fun getXxx(): Result<Xxx> =
        safeApiCall { api.getXxx().toDomain() }
}
```

---

## PRE-GENERATION CHECKLIST
Before writing any code, verify:

1. Does the new class have a single reason to change? If not — split it.
2. Does the new code belong in an existing module? Check OWNERSHIP table first.
3. Is the dependency direction correct? Check DEPENDENCY DIRECTION.
4. Does any ViewModel reference `NavController`? Replace with `AppNavigator`.
5. Is `UiState` a sealed interface? Never boolean flags.
6. Are `@Binds` / `@Provides` inside `feature:x/di/`, not in `core:network` or `core:database`?
7. Do DTOs carry `@SerializedName`? Do domain models have zero Gson annotations?
8. Are images loaded via Coil only?
