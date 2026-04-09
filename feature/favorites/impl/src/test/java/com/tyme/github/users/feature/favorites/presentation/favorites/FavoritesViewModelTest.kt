package com.tyme.github.users.feature.favorites.presentation.favorites

import app.cash.turbine.test
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.common.providers.DispatchersProvider
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
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
                com.tyme.github.users.core.common.models.UserModel(
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
    fun `onRemoveFavoriteClick sets ConfirmRemoval state`() = runTest {
        val userListItem = UserListItem(username = "user1")
        val viewModel = createViewModel()

        viewModel.onRemoveFavoriteClick(userListItem)

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.ConfirmRemoval(userListItem)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onConfirmRemoveFavorite calls removeFavoriteUseCase and resets to Idle`() = runTest {
        val userListItem = UserListItem(username = "user1")
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.success(Unit)
        val viewModel = createViewModel()
        viewModel.onRemoveFavoriteClick(userListItem)

        viewModel.onConfirmRemoveFavorite()

        coVerify { removeFavoriteUseCase(userListItem.username) }
        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onConfirmRemoveFavorite emits RemovalError when removal fails`() = runTest {
        val userListItem = UserListItem(username = "user1")
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.failure(RuntimeException("db error"))
        val viewModel = createViewModel()
        viewModel.onRemoveFavoriteClick(userListItem)

        viewModel.onConfirmRemoveFavorite()

        viewModel.uiState.test {
            awaitItem().shouldBeInstanceOf<FavoritesUiState.RemovalError>()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onConfirmRemoveFavorite does nothing when state is Idle`() = runTest {
        val viewModel = createViewModel()

        viewModel.onConfirmRemoveFavorite()

        coVerify(exactly = 0) { removeFavoriteUseCase(any()) }
    }

    @Test
    fun `onDismissRemoveFavorite resets state to Idle`() = runTest {
        val userListItem = UserListItem(username = "user1")
        val viewModel = createViewModel()
        viewModel.onRemoveFavoriteClick(userListItem)

        viewModel.onDismissRemoveFavorite()

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDismissError resets state to Idle`() = runTest {
        val userListItem = UserListItem(username = "user1")
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.failure(RuntimeException())
        val viewModel = createViewModel()
        viewModel.onRemoveFavoriteClick(userListItem)
        viewModel.onConfirmRemoveFavorite()

        viewModel.onDismissError()

        viewModel.uiState.test {
            awaitItem() shouldBe FavoritesUiState.Idle
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onNavigateToUser navigates to UserDetailsDestination`() = runTest {
        val userListItem = UserListItem(
            username = "user1",
            avatarUrl = "avatar",
            url = "https://github.com/user1",
        )
        coEvery { navigator.navigate(any()) } just runs
        val viewModel = createViewModel()

        viewModel.onNavigateToUser(userListItem)

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
}
