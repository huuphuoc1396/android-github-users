package com.example.github.users.feature.favorites.domain.usecase

import com.example.github.users.feature.favorites.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class RemoveFavoriteUseCaseImplTest {

    private val repository = mockk<FavoriteRepository>()

    private val useCase = RemoveFavoriteUseCaseImpl(repository)

    @Test
    fun `invoke calls repository removeFavorite with given username`() = runTest {
        // Given
        val username = "user1"
        coEvery { repository.removeFavorite(username) } returns Result.success(Unit)

        // When
        useCase(username)

        // Then
        coVerify { repository.removeFavorite(username) }
    }
}
