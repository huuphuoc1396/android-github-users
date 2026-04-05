package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import com.tyme.github.users.feature.users.domain.usecase.RemoveFavoriteUseCase
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class RemoveFavoriteUseCaseTest {

    private val repository = mockk<FavoriteRepository>()

    private val useCase = RemoveFavoriteUseCase(repository)

    @Test
    fun `invoke calls repository removeFavorite with given username`() = runTest {
        // Given
        val username = "user1"
        coJustRun { repository.removeFavorite(username) }

        // When
        useCase(username)

        // Then
        coVerify { repository.removeFavorite(username) }
    }
}
