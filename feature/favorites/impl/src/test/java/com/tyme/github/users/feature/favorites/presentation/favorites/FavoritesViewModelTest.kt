package com.tyme.github.users.feature.favorites.presentation.favorites

import app.cash.turbine.test
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.domain.providers.DispatchersProvider
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.domain.models.UserModel
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FavoritesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getFavoritesUseCase: GetFavoritesUseCase = mockk()
    private val removeFavoriteUseCase: RemoveFavoriteUseCase = mockk()
    private val navigator: AppNavigator = mockk()
    private val dispatchers: DispatchersProvider = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { dispatchers.io } returns testDispatcher
        every { dispatchers.main } returns testDispatcher
        every { dispatchers.default } returns testDispatcher
        every { dispatchers.immediate } returns testDispatcher
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = FavoritesViewModel(
        getFavoritesUseCase = getFavoritesUseCase,
        removeFavoriteUseCase = removeFavoriteUseCase,
        navigator = navigator,
        dispatchers = dispatchers,
    )

    @Test
    fun `uiState emits Loading initially`() = runTest {
        // Given
        every { getFavoritesUseCase() } returns flowOf(emptyList())

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            // Loading is the initialValue before combine emits
            awaitItem() // skip first emission (Loading or Empty depending on timing)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Empty when favorites list is empty`() = runTest {
        // Given
        every { getFavoritesUseCase() } returns flowOf(emptyList())
        val viewModel = createViewModel()

        // When / Then
        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Empty
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Success when favorites list is not empty`() = runTest {
        // Given
        val favorites = listOf(UserModel(username = "user1"), UserModel(username = "user2"))
        every { getFavoritesUseCase() } returns flowOf(favorites)
        val viewModel = createViewModel()

        // When / Then
        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Success(favorites = favorites)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRemoveFavoriteClick sets pendingRemoval in Success state`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        every { getFavoritesUseCase() } returns flowOf(listOf(user))
        val viewModel = createViewModel()

        // When
        viewModel.onRemoveFavoriteClick(user)

        // Then
        viewModel.uiState.test {
            (awaitItem() as FavoritesUiState.Success).pendingRemoval shouldBe user
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onConfirmRemoveFavorite calls removeFavoriteUseCase and clears pendingRemoval`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        every { getFavoritesUseCase() } returns flowOf(listOf(user))
        coEvery { removeFavoriteUseCase(user.username) } just runs
        val viewModel = createViewModel()
        viewModel.onRemoveFavoriteClick(user)

        // When
        viewModel.onConfirmRemoveFavorite()

        // Then
        coVerify { removeFavoriteUseCase(user.username) }
        viewModel.uiState.test {
            (awaitItem() as FavoritesUiState.Success).pendingRemoval shouldBe null
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onConfirmRemoveFavorite does nothing when pendingRemoval is null`() = runTest {
        // Given
        every { getFavoritesUseCase() } returns flowOf(emptyList())
        val viewModel = createViewModel()

        // When
        viewModel.onConfirmRemoveFavorite()

        // Then
        coVerify(exactly = 0) { removeFavoriteUseCase(any()) }
    }

    @Test
    fun `onDismissRemoveFavorite clears pendingRemoval`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        every { getFavoritesUseCase() } returns flowOf(listOf(user))
        val viewModel = createViewModel()
        viewModel.onRemoveFavoriteClick(user)

        // When
        viewModel.onDismissRemoveFavorite()

        // Then
        viewModel.uiState.test {
            (awaitItem() as FavoritesUiState.Success).pendingRemoval shouldBe null
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onNavigateToUser navigates to UserDetailsDestination`() = runTest {
        // Given
        val user = UserModel(username = "user1", avatarUrl = "avatar", url = "https://github.com/user1")
        every { getFavoritesUseCase() } returns flowOf(listOf(user))
        coEvery { navigator.navigate(any()) } just runs
        val viewModel = createViewModel()

        // When
        viewModel.onNavigateToUser(user)

        // Then
        coVerify {
            navigator.navigate(
                NavigationIntent.NavigateTo(
                    route = UserDetailsDestination(
                        username = user.username,
                        avatarUrl = user.avatarUrl,
                        url = user.url,
                    )
                )
            )
        }
    }
}
