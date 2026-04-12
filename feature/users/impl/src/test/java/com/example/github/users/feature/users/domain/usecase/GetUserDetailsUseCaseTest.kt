package com.example.github.users.feature.users.domain.usecase

import app.cash.turbine.test
import com.example.github.users.feature.users.domain.model.UserDetailsModel
import com.example.github.users.feature.users.domain.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class GetUserDetailsUseCaseTest {

    private val userRepository = mockk<UserRepository>()

    private val useCase = GetUserDetailsUseCase(userRepository)

    @Test
    fun `getUserDetails returns UserDetailsModel`() = runTest {
        // Given
        val username = "username"
        val userDetails = mockk<UserDetailsModel>()
        every { userRepository.getUserDetails(username) } returns flowOf(userDetails)

        // When
        val result: Flow<UserDetailsModel> = useCase(username)

        // Then
        result.test {
            expectMostRecentItem() shouldBe userDetails
        }
    }
}
