package com.tyme.github.users.feature.favorites.domain.usecase

import app.cash.turbine.test
import com.tyme.github.users.feature.favorites.domain.repository.FavoriteRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class IsFavoriteUseCaseImplTest {

    private val repository = mockk<FavoriteRepository>()

    private val useCase = IsFavoriteUseCaseImpl(repository)

    @Test
    fun `invoke emits true when username is favorite`() = runTest {
        // Given
        val username = "user1"
        every { repository.isFavorite(username) } returns flowOf(true)

        // When / Then
        useCase(username).test {
            expectMostRecentItem() shouldBe true
        }
    }

    @Test
    fun `invoke emits false when username is not favorite`() = runTest {
        // Given
        val username = "user1"
        every { repository.isFavorite(username) } returns flowOf(false)

        // When / Then
        useCase(username).test {
            expectMostRecentItem() shouldBe false
        }
    }

    @Test
    fun `invoke emits multiple values as favorite status changes`() = runTest {
        // Given
        val username = "user1"
        every { repository.isFavorite(username) } returns flowOf(false, true, false)

        // When / Then
        useCase(username).test {
            awaitItem() shouldBe false
            awaitItem() shouldBe true
            awaitItem() shouldBe false
            awaitComplete()
        }
    }
}
