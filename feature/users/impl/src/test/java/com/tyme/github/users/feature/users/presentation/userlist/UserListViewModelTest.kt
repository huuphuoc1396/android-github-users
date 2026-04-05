package com.tyme.github.users.feature.users.presentation.userlist

import androidx.paging.LoadState
import app.cash.turbine.test
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.domain.providers.DispatchersProvider
import com.tyme.github.users.feature.users.api.model.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import com.tyme.github.users.feature.users.domain.usecase.GetUserPagingUseCase
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    private val favoriteRepository: FavoriteRepository = mockk()
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
        every { favoriteRepository.getFavorites() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = UserListViewModel(
        getUserPagingUseCase = getUserPagingUseCase,
        favoriteRepository = favoriteRepository,
        navigator = navigator,
        dispatchers = dispatchers,
    )

    @Test
    fun `initial uiState is Idle`() = runTest {
        // When
        val viewModel = createViewModel()

        // Then
        viewModel.uiState.value shouldBe UserListUiState.Idle
    }

    @Test
    fun `onRefreshLoadState Loading when current is Idle sets Loading`() = runTest {
        // Given
        val viewModel = createViewModel()

        // When
        viewModel.onRefreshLoadState(LoadState.Loading)

        // Then
        viewModel.uiState.value shouldBe UserListUiState.Loading
    }

    @Test
    fun `onRefreshLoadState Loading when current is Success keeps Success`() = runTest {
        // Given
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        // When
        viewModel.onRefreshLoadState(LoadState.Loading)

        // Then
        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onRefreshLoadState NotLoading when current is Success sets isRefreshing false`() = runTest {
        // Given
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))
        viewModel.onRefreshTriggered()

        // When
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        // Then
        (viewModel.uiState.value as UserListUiState.Success).isRefreshing shouldBe false
    }

    @Test
    fun `onRefreshLoadState NotLoading when current is not Success sets Success`() = runTest {
        // Given
        val viewModel = createViewModel()

        // When
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        // Then
        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onRefreshLoadState Error sets Error state`() = runTest {
        // Given
        val viewModel = createViewModel()

        // When
        viewModel.onRefreshLoadState(LoadState.Error(Exception("network error")))

        // Then
        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Error>()
    }

    @Test
    fun `onRefreshTriggered when Success sets isRefreshing true`() = runTest {
        // Given
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        // When
        viewModel.onRefreshTriggered()

        // Then
        (viewModel.uiState.value as UserListUiState.Success).isRefreshing shouldBe true
    }

    @Test
    fun `onRefreshTriggered when not Success sets Loading`() = runTest {
        // Given
        val viewModel = createViewModel()

        // When
        viewModel.onRefreshTriggered()

        // Then
        viewModel.uiState.value shouldBe UserListUiState.Loading
    }

    @Test
    fun `dismissError resets to Success`() = runTest {
        // Given
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.Error(Exception()))

        // When
        viewModel.dismissError()

        // Then
        viewModel.uiState.value.shouldBeInstanceOf<UserListUiState.Success>()
    }

    @Test
    fun `onFavoriteClick when user is favorite sets pendingRemoval`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        every { favoriteRepository.getFavorites() } returns flowOf(listOf(user))
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))
        backgroundScope.launch { viewModel.favoriteUsernames.collect {} }
        advanceUntilIdle()

        // When
        viewModel.onFavoriteClick(user)

        // Then
        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe user
    }

    @Test
    fun `onFavoriteClick when user is not favorite calls addFavorite`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        coEvery { favoriteRepository.addFavorite(user) } just runs
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))

        // When
        viewModel.onFavoriteClick(user)

        // Then
        coVerify { favoriteRepository.addFavorite(user) }
    }

    @Test
    fun `onConfirmRemoveFavorite calls removeFavorite and clears pendingRemoval`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        every { favoriteRepository.getFavorites() } returns flowOf(listOf(user))
        coEvery { favoriteRepository.removeFavorite(user.username) } just runs
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))
        backgroundScope.launch { viewModel.favoriteUsernames.collect {} }
        advanceUntilIdle()
        viewModel.onFavoriteClick(user)

        // When
        viewModel.onConfirmRemoveFavorite()

        // Then
        coVerify { favoriteRepository.removeFavorite(user.username) }
        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe null
    }

    @Test
    fun `onDismissRemoveFavorite clears pendingRemoval`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        every { favoriteRepository.getFavorites() } returns flowOf(listOf(user))
        val viewModel = createViewModel()
        viewModel.onRefreshLoadState(LoadState.NotLoading(false))
        backgroundScope.launch { viewModel.favoriteUsernames.collect {} }
        advanceUntilIdle()
        viewModel.onFavoriteClick(user)

        // When
        viewModel.onDismissRemoveFavorite()

        // Then
        (viewModel.uiState.value as UserListUiState.Success).pendingRemoval shouldBe null
    }

    @Test
    fun `onNavigateToUser navigates to UserDetailsDestination`() = runTest {
        // Given
        val user = UserModel(username = "user1", avatarUrl = "avatar", url = "https://github.com/user1")
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

    @Test
    fun `favoriteUsernames emits username set from repository`() = runTest {
        // Given
        val user = UserModel(username = "user1")
        every { favoriteRepository.getFavorites() } returns flowOf(listOf(user))
        val viewModel = createViewModel()

        // When / Then
        viewModel.favoriteUsernames.test {
            expectMostRecentItem() shouldBe setOf("user1")
        }
    }
}
