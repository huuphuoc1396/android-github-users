package com.tyme.github.users

import androidx.lifecycle.SavedStateHandle
import com.tyme.github.users.providers.dispatchers.DispatchersProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

/**
 * A base test class for ViewModel testing with coroutines, using [StandardTestDispatcher] as the test dispatcher.
 *
 * This class provides a setup and teardown mechanism to replace the main dispatcher with a test dispatcher
 * for coroutine-based tests. It also mocks a [DispatchersProvider] to return the test dispatcher for
 * the `io` dispatcher.
 *
 * **Setup Flow:**
 * - The main dispatcher is replaced with [testDispatcher] using `Dispatchers.setMain()`.
 * - The [dispatchersProvider.io] is mocked to return the same [testDispatcher].
 *
 * **Tear Down Flow:**
 * - After the test, the main dispatcher is reset to its original value using `Dispatchers.resetMain()`.
 *
 * The `testDispatcher` allows you to control the execution of coroutines during tests and test asynchronous code in a deterministic manner.
 *
 * **Usage Example:**
 * ```kotlin
 * class MyViewModelTest : ViewModelTest() {
 *     @Test
 *     fun testMyViewModelFunction() {
 *         // Your test code here
 *     }
 * }
 * ```
 */
@ExperimentalCoroutinesApi
internal abstract class ViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    protected val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    protected val dispatchersProvider: DispatchersProvider = mockk()

    @Before
    fun setUp() {
        mockkSavedStateHandle()
        Dispatchers.setMain(testDispatcher)
        every { dispatchersProvider.io } returns testDispatcher
    }

    @After
    fun tearDown() {
        unmockkSavedStateHandle()
        Dispatchers.resetMain()
    }

    private fun mockkSavedStateHandle() = mockkStatic("androidx.navigation.SavedStateHandleKt")

    private fun unmockkSavedStateHandle() = unmockkStatic("androidx.navigation.SavedStateHandleKt")
}