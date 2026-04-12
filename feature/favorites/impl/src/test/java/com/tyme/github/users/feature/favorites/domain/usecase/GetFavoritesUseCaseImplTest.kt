package com.tyme.github.users.feature.favorites.domain.usecase

import app.cash.turbine.test
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.favorites.domain.repository.FavoriteRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class GetFavoritesUseCaseImplTest {

    private val repository = mockk<FavoriteRepository>()

    private val useCase = GetFavoritesUseCaseImpl(repository)

    @Test
    fun `invoke emits list of favorites from repository`() = runTest {
        // Given
        val favorites = listOf(
            UserModel(id = 1, username = "user1", isFavorite = true),
            UserModel(id = 2, username = "user2", isFavorite = true),
        )
        every { repository.getFavorites() } returns flowOf(favorites)

        // When / Then
        useCase().test {
            expectMostRecentItem() shouldBe favorites
        }
    }

    @Test
    fun `invoke emits empty list when there are no favorites`() = runTest {
        // Given
        every { repository.getFavorites() } returns flowOf(emptyList())

        // When / Then
        useCase().test {
            expectMostRecentItem() shouldBe emptyList()
        }
    }

    @Test
    fun `invoke emits multiple updates as favorites change`() = runTest {
        // Given
        val first = listOf(UserModel(id = 1, username = "user1", isFavorite = true))
        val second = listOf(
            UserModel(id = 1, username = "user1", isFavorite = true),
            UserModel(id = 2, username = "user2", isFavorite = true),
        )
        every { repository.getFavorites() } returns flowOf(first, second)

        // When / Then
        useCase().test {
            awaitItem() shouldBe first
            awaitItem() shouldBe second
            awaitComplete()
        }
    }
}
