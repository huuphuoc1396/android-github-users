package com.tyme.github.users.ui.features.users

import app.cash.turbine.test
import com.tyme.github.users.ViewModelTest
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.domain.usecases.users.GetUserListUseCase
import com.tyme.github.users.ui.features.users.models.UserListUiState
import com.tyme.github.users.ui.uistate.mappers.toErrorUiState
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UserListViewModelTest : ViewModelTest() {

    private val getUserListUseCase: GetUserListUseCase = mockk()

    private lateinit var userListViewModel: UserListViewModel

    @Test
    fun `getUserList is success`() = runTest {

        // Given
        val users = MutableList(10) { index -> UserModel(id = index) }
        every { getUserListUseCase() } returns flowOf(users)

        // When
        userListViewModel = UserListViewModel(
            dispatchersProvider = dispatchersProvider,
            getUserListUseCase = getUserListUseCase,
        )
        advanceUntilIdle()

        // Then
        userListViewModel.uiStateFlow.test {
            expectMostRecentItem() shouldBe UserListUiState(userList = users.toImmutableList())
        }
    }

    @Test
    fun `getUserList is failure`() = runTest {

        // Given
        val error = Exception("Error")
        every { getUserListUseCase() } returns flow { throw error }

        // When
        userListViewModel = UserListViewModel(
            dispatchersProvider = dispatchersProvider,
            getUserListUseCase = getUserListUseCase,
        )
        advanceUntilIdle()

        // Then
        userListViewModel.error.test {
            expectMostRecentItem() shouldBe error.toErrorUiState()
        }
    }
}