package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.domain.models.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import com.tyme.github.users.feature.users.domain.usecase.AddFavoriteUseCase
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class AddFavoriteUseCaseTest {

    private val repository = mockk<FavoriteRepository>()

    private val useCase = AddFavoriteUseCase(repository)

    @Test
    fun `invoke calls repository addFavorite with given user`() = runTest {
        // Given
        val user = UserModel(
            username = "user1",
            avatarUrl = "https://avatars.githubusercontent.com/u/1",
            url = "https://github.com/user1",
        )
        coJustRun { repository.addFavorite(user) }

        // When
        useCase(user)

        // Then
        coVerify { repository.addFavorite(user) }
    }
}
