package com.tyme.github.users.ui.features.users

import androidx.paging.PagingData
import app.cash.turbine.test
import com.tyme.github.users.ViewModelTest
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.domain.usecases.users.GetUserPagingUseCase
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UserListViewModelTest : ViewModelTest() {

    private val getUserPagingUseCase: GetUserPagingUseCase = mockk()

    private lateinit var userListViewModel: UserListViewModel

    @Test
    fun `userPaging is cached in viewModelScope`() = runTest {
        // Given
        every { getUserPagingUseCase() } returns flowOf(PagingData.empty())
        userListViewModel = UserListViewModel(
            getUserPagingUseCase = getUserPagingUseCase,
        )

        // When
        val userPaging = userListViewModel.userPaging
        advanceUntilIdle()

        // Then
        userPaging.test {
            expectMostRecentItem() should beInstanceOf<PagingData<UserModel>>()
        }
    }

    @Test
    fun `setRefreshing updates uiState`() = runTest {
        // Given
        every { getUserPagingUseCase() } returns flowOf(PagingData.empty())
        userListViewModel = UserListViewModel(
            getUserPagingUseCase = getUserPagingUseCase,
        )

        // When
        userListViewModel.setRefreshing(true)
        advanceUntilIdle()

        // Then
        userListViewModel.uiState.isRefreshing shouldBe true
    }

    @Test
    fun `setLoading doesn't call super when isRefreshing is true`() = runTest {
        // Given
        every { getUserPagingUseCase() } returns flowOf(PagingData.empty())
        userListViewModel = UserListViewModel(
            getUserPagingUseCase = getUserPagingUseCase,
        )

        // When
        userListViewModel.setRefreshing(true)
        userListViewModel.setLoading(true)

        advanceUntilIdle()

        // Then
        userListViewModel.isLoading.value shouldBe false
    }

    @Test
    fun `setLoading calls super when isRefreshing is false`() = runTest {
        // Given
        every { getUserPagingUseCase() } returns flowOf(PagingData.empty())
        userListViewModel = UserListViewModel(
            getUserPagingUseCase = getUserPagingUseCase,
        )

        // When
        userListViewModel.setRefreshing(false)
        userListViewModel.setLoading(true)

        advanceUntilIdle()

        // Then
        userListViewModel.isLoading.value shouldBe true
    }
}