package com.tyme.github.users.domain.usecases.users

import app.cash.turbine.test
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.domain.repositories.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class GetUserListUseCaseTest {

    private val userRepository = mockk<UserRepository>()

    private val useCase = GetUserListUseCase(userRepository)

    @Test
    fun `getUserList returns users`() = runTest {
        // Given
        val userList = mockk<List<UserModel>>()
        every { userRepository.getUserList() } returns flowOf(userList)

        // When
        val result: Flow<List<UserModel>> = useCase()

        // Then
        result.test {
            expectMostRecentItem() shouldBe userList
        }
    }
}