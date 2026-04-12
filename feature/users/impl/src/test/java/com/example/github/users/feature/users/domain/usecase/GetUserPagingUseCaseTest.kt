package com.example.github.users.feature.users.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.github.users.core.common.model.UserModel
import com.example.github.users.feature.users.domain.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class GetUserPagingUseCaseTest {

    private val userRepository = mockk<UserRepository>()

    private val useCase = GetUserPagingUseCase(userRepository)

    @Test
    fun `getUserPaging returns PagingData`() = runTest {
        // Given
        val userList = mockk<List<UserModel>>()
        val pagingData = PagingData.from(userList)
        every { userRepository.getUserPaging() } returns flowOf(pagingData)

        // When
        val result = useCase()

        // Then
        result.test {
            expectMostRecentItem() shouldBe pagingData
        }
    }
}
