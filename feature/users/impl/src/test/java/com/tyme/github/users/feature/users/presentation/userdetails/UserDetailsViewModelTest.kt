package com.tyme.github.users.feature.users.presentation.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.domain.providers.DispatchersProvider
import com.tyme.github.users.domain.models.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import com.tyme.github.users.feature.users.data.mapper.toUserDetailUiState
import com.tyme.github.users.feature.users.domain.model.UserDetailsModel
import com.tyme.github.users.feature.users.domain.usecase.GetUserDetailsUseCase
import com.tyme.github.users.feature.users.domain.usecase.ObserveFavoriteUseCase
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
internal class UserDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getUserDetailsUseCase: GetUserDetailsUseCase = mockk()
    private val observeFavoriteUseCase: ObserveFavoriteUseCase = mockk()
    private val favoriteRepository: FavoriteRepository = mockk()
    private val navigator: AppNavigator = mockk()
    private val dispatchers: DispatchersProvider = mockk()

    private val destination = UserDetailsDestination(
        username = "user1",
        avatarUrl = "avatar",
        url = "https://github.com/user1",
    )
    private val userDetails = UserDetailsModel(
        username = "user1",
        avatarUrl = "avatar",
        country = "VN",
        followers = 10,
        following = 5,
        url = "https://github.com/user1",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { dispatchers.io } returns testDispatcher
        every { dispatchers.main } returns testDispatcher
        every { dispatchers.default } returns testDispatcher
        every { dispatchers.immediate } returns testDispatcher
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<UserDetailsDestination>() } returns destination
        every { observeFavoriteUseCase(destination.username) } returns flowOf(false)
        every { favoriteRepository.isFavorite(destination.username) } returns flowOf(false)
        every { getUserDetailsUseCase(destination.username) } returns flowOf(userDetails)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    private fun createViewModel() = UserDetailsViewModel(
        savedStateHandle = savedStateHandle,
        getUserDetailsUseCase = getUserDetailsUseCase,
        observeFavoriteUseCase = observeFavoriteUseCase,
        favoriteRepository = favoriteRepository,
        navigator = navigator,
        dispatchers = dispatchers,
    )

    @Test
    fun `init loads user details and emits Success`() = runTest {
        // When
        val viewModel = createViewModel()

        // Then
        viewModel.uiState.value shouldBe userDetails.toUserDetailUiState()
    }

    @Test
    fun `loadUserDetails emits Loading then Success`() = runTest {
        // Given
        val viewModel = createViewModel()

        // When / Then
        viewModel.uiState.test {
            awaitItem() shouldBe userDetails.toUserDetailUiState()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadUserDetails emits Error on failure`() = runTest {
        // Given
        every { getUserDetailsUseCase(destination.username) } returns flow { throw Exception("network error") }

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.uiState.value.shouldBeInstanceOf<UserDetailUiState.Error>()
    }

    @Test
    fun `onFavoriteClick when isFavorite shows remove confirm dialog`() = runTest {
        // Given
        every { observeFavoriteUseCase(destination.username) } returns flowOf(true)
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.isFavorite.collect {} }
        advanceUntilIdle()

        // When
        viewModel.onFavoriteClick()

        // Then
        (viewModel.uiState.value as UserDetailUiState.Success).showRemoveConfirmDialog shouldBe true
    }

    @Test
    fun `onFavoriteClick when not favorite calls addFavorite`() = runTest {
        // Given
        val expectedUser = UserModel(
            username = destination.username,
            avatarUrl = destination.avatarUrl,
            url = destination.url,
        )
        coEvery { favoriteRepository.addFavorite(expectedUser) } just runs
        val viewModel = createViewModel()

        // When
        viewModel.onFavoriteClick()

        // Then
        coVerify { favoriteRepository.addFavorite(expectedUser) }
    }

    @Test
    fun `onConfirmRemoveFavorite calls removeFavorite and hides dialog`() = runTest {
        // Given
        every { observeFavoriteUseCase(destination.username) } returns flowOf(true)
        coEvery { favoriteRepository.removeFavorite(destination.username) } just runs
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.isFavorite.collect {} }
        advanceUntilIdle()
        viewModel.onFavoriteClick()

        // When
        viewModel.onConfirmRemoveFavorite()

        // Then
        coVerify { favoriteRepository.removeFavorite(destination.username) }
        (viewModel.uiState.value as UserDetailUiState.Success).showRemoveConfirmDialog shouldBe false
    }

    @Test
    fun `onDismissRemoveFavorite hides dialog`() = runTest {
        // Given
        every { observeFavoriteUseCase(destination.username) } returns flowOf(true)
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.isFavorite.collect {} }
        advanceUntilIdle()
        viewModel.onFavoriteClick()

        // When
        viewModel.onDismissRemoveFavorite()

        // Then
        (viewModel.uiState.value as UserDetailUiState.Success).showRemoveConfirmDialog shouldBe false
    }

    @Test
    fun `dismissError resets uiState to Idle`() = runTest {
        // Given
        every { getUserDetailsUseCase(destination.username) } returns flow { throw Exception() }
        val viewModel = createViewModel()

        // When
        viewModel.dismissError()

        // Then
        viewModel.uiState.value shouldBe UserDetailUiState.Idle
    }

    @Test
    fun `onNavigateBack navigates up`() = runTest {
        // Given
        coEvery { navigator.navigate(any()) } just runs
        val viewModel = createViewModel()

        // When
        viewModel.onNavigateBack()

        // Then
        coVerify { navigator.navigate(NavigationIntent.NavigateUp) }
    }

    @Test
    fun `isFavorite emits value from repository`() = runTest {
        // Given
        every { observeFavoriteUseCase(destination.username) } returns flowOf(true)

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.isFavorite.test {
            expectMostRecentItem() shouldBe true
        }
    }
}
