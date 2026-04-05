package com.tyme.github.users.feature.favorites.domain.usecase

import app.cash.turbine.test
import com.tyme.github.users.domain.models.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class GetFavoritesUseCaseTest {

    private val repository = mockk<FavoriteRepository>()

    private val useCase = GetFavoritesUseCase(repository)

    @Test
    fun `invoke emits list of favorites from repository`() = runTest {
        // Given
        val favorites = listOf(
            UserModel(
                username = "user1",
                avatarUrl = "https://avatars.githubusercontent.com/u/1",
                url = "https://github.com/user1",
            ),
            UserModel(
                username = "user2",
                avatarUrl = "https://avatars.githubusercontent.com/u/2",
                url = "https://github.com/user2",
            ),
        )
        every { repository.getFavorites() } returns flowOf(favorites)

        // When / Then
        useCase().test {
            expectMostRecentItem() shouldBe favorites
        }
    }

    @Test
    fun `invoke emits empty list when no favorites`() = runTest {
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
        val user = UserModel(
            username = "user1",
            avatarUrl = "https://avatars.githubusercontent.com/u/1",
            url = "https://github.com/user1",
        )
        every { repository.getFavorites() } returns flowOf(emptyList(), listOf(user))

        // When / Then
        useCase().test {
            awaitItem() shouldBe emptyList()
            awaitItem() shouldBe listOf(user)
            awaitComplete()
        }
    }
}
