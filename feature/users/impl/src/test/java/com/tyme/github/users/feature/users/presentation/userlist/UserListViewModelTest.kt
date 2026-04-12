package com.tyme.github.users.feature.users.presentation.userlist

import androidx.paging.LoadState
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.common.dispatcher.CoroutineDispatchers
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.feature.favorites.domain.usecase.AddFavoriteUseCase
import com.tyme.github.users.feature.users.domain.usecase.GetUserPagingUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
internal class UserListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getUserPagingUseCase: GetUserPagingUseCase = mockk()
    private val addFavoriteUseCase: AddFavoriteUseCase = mockk()
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
        every { getUserPagingUseCase() } returns flowOf()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = UserListViewModel(
        getUserPagingUseCase = getUserPagingUseCase,
        addFavoriteUseCase = addFavoriteUseCase,
        removeFavoriteUseCase = removeFavoriteUseCase,
        navigator = navigator,
        dispatchers = dispatchers,
    )

    @Test
    fun `initial uiState is Idle`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.value shouldBe UserListUiState.Idle
    }

    @Test
    fun `onAction RefreshLoadStateChanged Loading when current is Idle sets Loading`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.Loading))

        viewModel.uiState.value shouldBe UserListUiState.Loading
    }

    @Test
    fun `onAction RefreshLoadStateChanged Loading when current is Success keeps Success`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))

        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.Loading))

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onAction RefreshLoadStateChanged NotLoading when current is Success sets isRefreshing false`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))
        viewModel.onAction(UserListUiAction.Refresh)

        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))

        (viewModel.uiState.value as UserListUiState.Success).isRefreshing shouldBe false
    }

    @Test
    fun `onAction RefreshLoadStateChanged NotLoading when current is not Success sets Success`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onAction RefreshLoadStateChanged Error sets Error state`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.Error(Exception("network error"))))

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Error>()
    }

    @Test
    fun `onAction Refresh when Success sets isRefreshing true`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))

        viewModel.onAction(UserListUiAction.Refresh)

        (viewModel.uiState.value as UserListUiState.Success).isRefreshing shouldBe true
    }

    @Test
    fun `onAction Refresh when not Success sets Loading`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UserListUiAction.Refresh)

        viewModel.uiState.value shouldBe UserListUiState.Loading
    }

    @Test
    fun `onAction DismissError resets to Success`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.Error(Exception())))

        viewModel.onAction(UserListUiAction.DismissError)

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onAction FavoriteClick when user is favorite sets pendingRemoval`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))

        viewModel.onAction(UserListUiAction.FavoriteClick(userListItem))

        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe userListItem
    }

    @Test
    fun `onAction FavoriteClick when user is favorite but state is not Success does nothing`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        val viewModel = createViewModel()
        // state remains Idle (no NotLoading transition)

        viewModel.onAction(UserListUiAction.FavoriteClick(userListItem))

        viewModel.uiState.value shouldBe UserListUiState.Idle
    }

    @Test
    fun `onAction FavoriteClick when user is not favorite calls addFavorite`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = false)
        val userModel = UserModel(username = "user1")
        coEvery { addFavoriteUseCase(userModel) } returns Result.success(Unit)
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))

        viewModel.onAction(UserListUiAction.FavoriteClick(userListItem))

        coVerify { addFavoriteUseCase(userModel) }
    }

    @Test
    fun `onAction FavoriteClick when user is not favorite and addFavorite fails sets Error`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = false)
        val userModel = UserModel(username = "user1")
        coEvery { addFavoriteUseCase(userModel) } returns Result.failure(RuntimeException("db error"))
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))

        viewModel.onAction(UserListUiAction.FavoriteClick(userListItem))

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Error>()
    }

    @Test
    fun `onAction ConfirmRemoveFavorite calls removeFavorite and clears pendingRemoval`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.success(Unit)
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))
        viewModel.onAction(UserListUiAction.FavoriteClick(userListItem))

        viewModel.onAction(UserListUiAction.ConfirmRemoveFavorite)

        coVerify { removeFavoriteUseCase(userListItem.username) }
        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe null
    }

    @Test
    fun `onAction ConfirmRemoveFavorite does nothing when state is not Success`() = runTest {
        val viewModel = createViewModel()
        // state remains Idle (no NotLoading transition)

        viewModel.onAction(UserListUiAction.ConfirmRemoveFavorite)

        coVerify(exactly = 0) { removeFavoriteUseCase(any()) }
        viewModel.uiState.value shouldBe UserListUiState.Idle
    }

    @Test
    fun `onAction ConfirmRemoveFavorite does nothing when Success state has no pendingRemoval`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))
        // Success state with pendingRemoval = null

        viewModel.onAction(UserListUiAction.ConfirmRemoveFavorite)

        coVerify(exactly = 0) { removeFavoriteUseCase(any()) }
        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe null
    }

    @Test
    fun `onAction ConfirmRemoveFavorite sets Error when removeFavorite fails`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.failure(RuntimeException("db error"))
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))
        viewModel.onAction(UserListUiAction.FavoriteClick(userListItem))

        viewModel.onAction(UserListUiAction.ConfirmRemoveFavorite)

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Error>()
    }

    @Test
    fun `onAction DismissRemoveFavorite clears pendingRemoval`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        val viewModel = createViewModel()
        viewModel.onAction(UserListUiAction.RefreshLoadStateChanged(LoadState.NotLoading(false)))
        viewModel.onAction(UserListUiAction.FavoriteClick(userListItem))

        viewModel.onAction(UserListUiAction.DismissRemoveFavorite)

        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe null
    }

    @Test
    fun `onAction DismissRemoveFavorite when state is not Success does nothing`() = runTest {
        val viewModel = createViewModel()
        // state remains Idle (no NotLoading transition)

        viewModel.onAction(UserListUiAction.DismissRemoveFavorite)

        viewModel.uiState.value shouldBe UserListUiState.Idle
    }

    @Test
    fun `onAction UserClick navigates to UserDetailsDestination`() = runTest {
        val user = UserListItem(username = "user1", avatarUrl = "avatar", url = "https://github.com/user1")
        coEvery { navigator.navigate(any()) } returns Unit
        val viewModel = createViewModel()

        viewModel.onAction(UserListUiAction.UserClick(user))

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

    @Test
    fun `onAction UrlClick navigates to NavigationIntent OpenUrl`() = runTest {
        val url = "https://example.com"
        coEvery { navigator.navigate(any()) } returns Unit
        val viewModel = createViewModel()

        viewModel.onAction(UserListUiAction.UrlClick(url))

        coVerify { navigator.navigate(NavigationIntent.OpenUrl(url)) }
    }
}
