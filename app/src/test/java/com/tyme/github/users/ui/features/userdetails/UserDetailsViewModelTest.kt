package com.tyme.github.users.ui.features.userdetails

import androidx.navigation.toRoute
import app.cash.turbine.test
import com.tyme.github.users.ViewModelTest
import com.tyme.github.users.domain.models.users.UserDetailsModel
import com.tyme.github.users.domain.usecases.users.GetUserDetailsUseCase
import com.tyme.github.users.ui.features.userdetails.mappers.toUserDetailsUiState
import com.tyme.github.users.ui.features.userdetails.models.UserDetailsDestination
import com.tyme.github.users.ui.uistate.mappers.toErrorUiState
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class UserDetailsViewModelTest : ViewModelTest() {

    private val getUserDetailsUseCase = mockk<GetUserDetailsUseCase>()

    private lateinit var viewModel: UserDetailsViewModel

    @Test
    fun `getUserDetails is success`() = runTest {
        // Given
        val username = "username"
        val userDetailsDestination = UserDetailsDestination(username = username)
        val userDetails = UserDetailsModel(
            username = "username",
            avatarUrl = "avatarUrl",
            country = "country",
            followers = 1,
            following = 1,
            url = "url",
        )
        coEvery { getUserDetailsUseCase(username) } returns flowOf(userDetails)
        every { savedStateHandle.toRoute<UserDetailsDestination>() } returns userDetailsDestination

        // When
        viewModel = UserDetailsViewModel(
            savedStateHandle = savedStateHandle,
            dispatchersProvider = dispatchersProvider,
            getUserDetailsUseCase = getUserDetailsUseCase,
        )
        advanceUntilIdle()

        // Then
        viewModel.uiStateFlow.test {
            expectMostRecentItem() shouldBe userDetails.toUserDetailsUiState()
        }
    }

    @Test
    fun `getUserDetails is failure`() = runTest {
        // Given
        val username = "username"
        val userDetailsDestination = UserDetailsDestination(username = username)
        val error = Exception("Error")
        coEvery { getUserDetailsUseCase(username) } returns flow { throw error }
        every { savedStateHandle.toRoute<UserDetailsDestination>() } returns userDetailsDestination

        // When
        viewModel = UserDetailsViewModel(
            savedStateHandle = savedStateHandle,
            dispatchersProvider = dispatchersProvider,
            getUserDetailsUseCase = getUserDetailsUseCase,
        )
        advanceUntilIdle()

        // Then
        viewModel.error.test {
            expectMostRecentItem() shouldBe error.toErrorUiState()
        }
    }
}