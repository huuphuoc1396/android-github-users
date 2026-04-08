package com.tyme.github.users.feature.users.presentation.userlist

import androidx.paging.LoadState
import app.cash.turbine.test
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.common.providers.DispatchersProvider
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
    private val dispatchers: DispatchersProvider = mockk()

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
    fun `onRefreshLoadState Loading when current is Idle sets Loading`() = runTest {
        val viewModel = createViewModel()

        viewModel.onRefreshLoadState(LoadState.Loading)

        viewModel.uiState.value shouldBe UserListUiState.Loading
    }

    @Test
    fun `onRefreshLoadState Loading when current is Success keeps Success`() = runTest {
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        viewModel.onRefreshLoadState(LoadState.Loading)

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onRefreshLoadState NotLoading when current is Success sets isRefreshing false`() = runTest {
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))
        viewModel.onRefreshTriggered()

        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        (viewModel.uiState.value as UserListUiState.Success).isRefreshing shouldBe false
    }

    @Test
    fun `onRefreshLoadState NotLoading when current is not Success sets Success`() = runTest {
        val viewModel = createViewModel()

        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onRefreshLoadState Error sets Error state`() = runTest {
        val viewModel = createViewModel()

        viewModel.onRefreshLoadState(LoadState.Error(Exception("network error")))

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Error>()
    }

    @Test
    fun `onRefreshTriggered when Success sets isRefreshing true`() = runTest {
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        viewModel.onRefreshTriggered()

        (viewModel.uiState.value as UserListUiState.Success).isRefreshing shouldBe true
    }

    @Test
    fun `onRefreshTriggered when not Success sets Loading`() = runTest {
        val viewModel = createViewModel()

        viewModel.onRefreshTriggered()

        viewModel.uiState.value shouldBe UserListUiState.Loading
    }

    @Test
    fun `dismissError resets to Success`() = runTest {
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.Error(Exception()))

        viewModel.dismissError()

        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onFavoriteClick when user is favorite sets pendingRemoval`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        viewModel.onFavoriteClick(userListItem)

        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe userListItem
    }

    @Test
    fun `onFavoriteClick when user is not favorite calls addFavorite`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = false)
        val userModel = UserModel(username = "user1")
        coEvery { addFavoriteUseCase(userModel) } returns Result.success(Unit)
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        viewModel.onFavoriteClick(userListItem)

        coVerify { addFavoriteUseCase(userModel) }
    }

    @Test
    fun `onConfirmRemoveFavorite calls removeFavorite and clears pendingRemoval`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        coEvery { removeFavoriteUseCase(userListItem.username) } returns Result.success(Unit)
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))
        viewModel.onFavoriteClick(userListItem)

        viewModel.onConfirmRemoveFavorite()

        coVerify { removeFavoriteUseCase(userListItem.username) }
        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe null
    }

    @Test
    fun `onDismissRemoveFavorite clears pendingRemoval`() = runTest {
        val userListItem = UserListItem(username = "user1", isFavorite = true)
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))
        viewModel.onFavoriteClick(userListItem)

        viewModel.onDismissRemoveFavorite()

        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe null
    }

    @Test
    fun `onNavigateToUser navigates to UserDetailsDestination`() = runTest {
        val user = UserListItem(username = "user1", avatarUrl = "avatar", url = "https://github.com/user1")
        coEvery { navigator.navigate(any()) } returns Unit
        val viewModel = createViewModel()

        viewModel.onNavigateToUser(user)

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
