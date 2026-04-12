package com.example.github.users.feature.favorites.presentation.favorites

import app.cash.turbine.test
import com.example.github.users.core.common.dispatcher.CoroutineDispatchers
import com.example.github.users.core.navigation.AppNavigator
import com.example.github.users.core.navigation.NavigationIntent
import com.example.github.users.core.ui.component.UserListItem
import com.example.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.example.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.example.github.users.feature.users.navigation.UserDetailsDestination
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
    private val dispatchers: CoroutineDispatchers = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { dispatchers.io } returns testDispatcher
        every { dispatchers.main } returns testDispatcher
        every { dispatchers.default } returns testDispatcher
        every { dispatchers.immediate } returns testDispatcher
        every { getFavoritesUseCase() } returns flowOf(emptyList())
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
    fun `uiState starts as Idle`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `favorites emits mapped list from use case`() = runTest {
        // Given
        val userListItems = listOf(
            UserListItem(id = 1, username = "user1", isFavorite = true),
            UserListItem(id = 2, username = "user2", isFavorite = true),
        )
        every { getFavoritesUseCase() } returns flowOf(
            userListItems.map {
                com.example.github.users.core.common.model.UserModel(
                    id = it.id,
                    username = it.username,
                    isFavorite = it.isFavorite,
                )
            }
        )
        val viewModel = createViewModel()

        viewModel.favorites.test {
            awaitItem() shouldBe userListItems
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `favorites sets uiState to LoadError when getFavoritesUseCase throws`() = runTest {
        every { getFavoritesUseCase() } returns flow { throw RuntimeException("stream error") }
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle

            backgroundScope.launch {
                viewModel.favorites.collect {}
            }

            awaitItem().shouldBeInstanceOf<FavoritesUiState.LoadError>()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction RemoveFavoriteClick sets ConfirmRemoval state`() = runTest {
        val userListItem = UserListItem(username = "user1")
        val viewModel = createViewModel()

        viewModel.onAction(FavoritesUiAction.RemoveFavoriteClick(userListItem))

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.ConfirmRemoval(userListItem)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction ConfirmRemoveFavorite calls removeFavoriteUseCase and resets to Idle`() = runTest {
        val userListItem = UserListItem(username = "user1")
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.success(Unit)
        val viewModel = createViewModel()
        viewModel.onAction(FavoritesUiAction.RemoveFavoriteClick(userListItem))

        viewModel.onAction(FavoritesUiAction.ConfirmRemoveFavorite)

        coVerify { removeFavoriteUseCase(userListItem.username) }
        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction ConfirmRemoveFavorite emits RemovalError when removal fails`() = runTest {
        val userListItem = UserListItem(username = "user1")
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.failure(RuntimeException("db error"))
        val viewModel = createViewModel()
        viewModel.onAction(FavoritesUiAction.RemoveFavoriteClick(userListItem))

        viewModel.onAction(FavoritesUiAction.ConfirmRemoveFavorite)

        viewModel.uiState.test {
            awaitItem().shouldBeInstanceOf<FavoritesUiState.RemovalError>()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction ConfirmRemoveFavorite does nothing when state is Idle`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(FavoritesUiAction.ConfirmRemoveFavorite)

        coVerify(exactly = 0) { removeFavoriteUseCase(any()) }
    }

    @Test
    fun `onAction DismissRemoveFavorite resets state to Idle`() = runTest {
        val userListItem = UserListItem(username = "user1")
        val viewModel = createViewModel()
        viewModel.onAction(FavoritesUiAction.RemoveFavoriteClick(userListItem))

        viewModel.onAction(FavoritesUiAction.DismissRemoveFavorite)

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction DismissError resets state to Idle`() = runTest {
        val userListItem = UserListItem(username = "user1")
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.failure(RuntimeException())
        val viewModel = createViewModel()
        viewModel.onAction(FavoritesUiAction.RemoveFavoriteClick(userListItem))
        viewModel.onAction(FavoritesUiAction.ConfirmRemoveFavorite)

        viewModel.onAction(FavoritesUiAction.DismissError)

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction UserClick navigates to UserDetailsDestination`() = runTest {
        val userListItem = UserListItem(
            username = "user1",
            avatarUrl = "avatar",
            url = "https://github.com/user1",
        )
        coEvery { navigator.navigate(any()) } just runs
        val viewModel = createViewModel()

        viewModel.onAction(FavoritesUiAction.UserClick(userListItem))

        coVerify {
            navigator.navigate(
                NavigationIntent.NavigateTo(
                    route = UserDetailsDestination(
                        username = userListItem.username,
                        avatarUrl = userListItem.avatarUrl,
                        url = userListItem.url,
                    )
                )
            )
        }
    }

    @Test
    fun `onAction UrlClick navigates to NavigationIntent OpenUrl`() = runTest {
        val url = "https://example.com"
        coEvery { navigator.navigate(any()) } just runs
        val viewModel = createViewModel()

        viewModel.onAction(FavoritesUiAction.UrlClick(url))

        coVerify { navigator.navigate(NavigationIntent.OpenUrl(url)) }
    }
}
