package com.tyme.github.users.data.repositories.users

import app.cash.turbine.test
import com.tyme.github.users.data.remote.responses.users.UserResponse
import com.tyme.github.users.data.remote.services.UserService
import com.tyme.github.users.data.repositories.users.mappers.toUserModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class UserRepositoryImplTest {

    private val userService: UserService = mockk()

    private val userRepository = UserRepositoryImpl(userService)

    @Test
    fun `getUserList returns users`() = runTest {
        // Given
        val responses = listOf(UserResponse())
        val expected = responses.map { response -> response.toUserModel() }
        coEvery { userService.getUsers() } returns responses

        // When
        val result = userRepository.getUserList()

        // Then
        result.test {
            expectMostRecentItem() shouldBe expected
        }
    }
}