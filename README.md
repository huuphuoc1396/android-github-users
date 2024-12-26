# GitHub Users

Welcome to the GitHub Users application. This app allows an administrator to browse all users of the GitHub site and view more detailed information about them.

## Features

### User List

- View all users on the GitHub site. 
- This list will be displayed with pagination, showing 20 items per page. 
- Users can also use the app offline to view the previously loaded user list.

&emsp;<img src="https://github.com/user-attachments/assets/7e409eb9-a6bd-4938-9343-dd95d365aa4b" width=164>
&emsp;<img src="https://github.com/user-attachments/assets/d3d1ab82-61af-4365-8e40-bf546275a64a" width=164>

- If loading the next page fails, users have the option to try again.

&emsp;<img src="https://github.com/user-attachments/assets/79450fe3-8cdc-460c-8196-406880678693" width=164>

### User Details

- View detailed information about the user such as their name, avatar, followers, etc.
- Users can also access basic information while in offline mode.

&emsp;<img src="https://github.com/user-attachments/assets/819db123-34c7-4c89-9b05-a53ae237c7f3" width=164>
&emsp;<img src="https://github.com/user-attachments/assets/c58f79c4-5891-496f-a809-b1bef3173ae5" width=164>


### Theme

- Supports [Dark Mode](https://developer.android.com/develop/ui/views/theming/darktheme)

&emsp;<img src="https://github.com/user-attachments/assets/86fe4a6c-ba08-465e-be36-3f8cb68c7c27" width=164>
&emsp;<img src="https://github.com/user-attachments/assets/ffe38b79-68c1-459d-b479-7d319398f42f" width=164>

- Supports [Dynamic Color](https://developer.android.com/develop/ui/views/theming/dynamic-colors) (Android 12+)

&emsp;<img src="https://github.com/user-attachments/assets/1a83be53-7128-4f9e-8187-1b82699d3d8b" width=164>
&emsp;<img src="https://github.com/user-attachments/assets/5ea014b1-9cb3-4882-8012-9d8cdc73be84" width=164>

### Deep Links

Supports deep links:

- User List: `https://github-users.tyme.com/users`

  ```bash
  adb shell am start -a android.intent.action.VIEW -d "https://github-users.tyme.com/users"   
  ```
- User Details: `https://github-users.tyme.com/users/{username}`

   ```bash
  adb shell am start -a android.intent.action.VIEW -d "https://github-users.tyme.com/users/huuphuoc1396"   
  ```

_**Note:** `https://github-users.tyme.com` is a demo link that has not been verified, which is why it doesn't add automatically to the supported links. Please ensure you have manually added it in the app settings._

&emsp;<img src="https://github.com/user-attachments/assets/53c7cf96-2c73-49ba-ad40-3ea714db4acd" width=164>
&emsp;&#8594;&emsp;<img src="https://github.com/user-attachments/assets/b23f37db-78ac-4bf9-a31f-ebf0a2b01b77" width=164>

  
## Technical Uses

### App Architecture

Clean Architecture: Ensures a robust, scalable, and testable codebase. There are three modules mainly: 
- **`app`** (presentation): UI components and ViewModels.
- **`domain`**: Use cases, models, and repository interfaces.
- **`data`**: Data sources, repository implementations, and mappers.

The project implements separation models for the corresponding modules, ensuring that each one adheres to the principle of single responsibility.
- The `app` module will contain the `UiState` model, which is responsible for managing the states of composable components. This model may include the `domain` model if it is stable for composables or may be mapped to a new `UiState` model to achieve [Stability in Compose](https://developer.android.com/develop/ui/compose/performance/stability).
- The `data` module will include two types of models: The `Response` model for handling API responses and the `Entity` model for the Room database.
- The `domain` model, utilized in both the `data` and `app` modules, will provide clarity for the business logic.

The project supports three product flavors: `dev` for development, `stag` for staging, and `prod` for production.

### UI Components

[Jetpack Compose](https://developer.android.com/jetpack/compose): Modern toolkit for building native UIs with less code. The codebase supports `UiStateScreen` composable and `UiStateViewModel`, which helps reduce boilerplate code and manage UI state, loading state, error state, and single events more efficiently.

### Navigation

[Navigation Component](https://developer.android.com/jetpack/compose/navigation): Facilitates navigation and data transfer between destinations. It integrates with Kotlin serialization to ensure type-safe navigation arguments, making the implementation of navigation routes easier.

### Dependency Injection

[Hilt](https://developer.android.com/training/dependency-injection/hilt-android): A dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in the project.

### Network and Database

Network: Using [Retrofit](https://square.github.io/retrofit/) as a type-safe HTTP client along with [Gson](https://github.com/google/gson) facilitates data response parsing. A custom `ErrorHandlingCallAdapter` is included to process the response and properly map any errors.

Database: Using the [Room](https://developer.android.com/training/data-storage/room) database with [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) facilitates pagination and caches data for offline use. This approach also reduces development time compared to manual pagination handling.

### Asynchronous Handling

Simplifies asynchronous programming and stream handling using Kotlin **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** and **[Flow](https://developer.android.com/kotlin/flow)**. `UiStateViewModel` supports two methods:
- `launchSafe`: Launches a coroutine with error handling and optional loading state management.
- `Flow<T>.collectSafe`: Collects a Flow with error handling and optional loading state management.

### Unit Testing

Utilizing [JUnit](https://junit.org/junit4/), [MockK](https://mockk.io/), [Turbine](https://github.com/cashapp/turbine), and [Kotest](https://kotest.io/) to support Unit Testing. The test cases will cover:
- `app`: All ViewModels, Mappers, and utility methods
- `domain`: All UseCases and utility methods 
- `data`: All Repositories and Mappers.

### Security

- Network: SSL Pinning has been enabled to enhance the security of services or sites that utilize SSL Certificates. By using the pinned public key, this approach prevents websites from rotating their certificates, thereby minimizing the risk of rendering the app unusable.
- Database: [SQLCipher](https://github.com/sqlcipher/sqlcipher-android) is employed to secure the local database. It is disabled in the development environment to facilitate app inspection, while it is enabled in both the staging and production environments to test functionality in real-world scenarios.
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore): [Encrypted DataStore](https://github.com/osipxd/encrypted-datastore) to secure the sensitive data stored such as the access token, user ID, signature, etc.
- [R8](https://developer.android.com/build/shrink-code): Shrinking, obfuscation, and optimization have been enabled to improve code efficiency and security.

### Others

[LeakCanary](https://square.github.io/leakcanary/) has been enabled in the debug to automatically detect leaks of the following objects:
- Destroyed `Activity` instances
- Destroyed `Fragment` instances
- Destroyed fragment `View` instances
- Cleared `ViewModel` instances
- Destroyed `Service` instance

### Prerequisites

- Android Studio Ladybug+
- JDK 11+
- Android Gradle Plugin 8.6.1+
- Kotlin 2.0.0+

### Get Started

1. Clone the repository.
   ```bash
   git clone https://github.com/huuphuoc1396/android-github-users.git
   ```
2. Open the `android-github-users/` directory in Android Studio.
3. Copy the `credentials.cpp` to `data/cpp`.
4. Select the Build Variants.
5. Run `app`. 

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Contact

For any inquiries, please reach out to [Phuoc Bui](https://github.com/huuphuoc1396).
