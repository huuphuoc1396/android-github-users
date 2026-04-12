package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class AddFavoriteUseCaseImplTest {

    private val repository = mockk<FavoriteRepository>()

    private val useCase = AddFavoriteUseCaseImpl(repository)

    @Test
    fun `invoke calls repository addFavorite with given user`() = runTest {
        // Given
        val user = UserModel(
            username = "user1",
            avatarUrl = "https://avatars.githubusercontent.com/u/1",
            url = "https://github.com/user1",
        )
        coEvery { repository.addFavorite(user) } returns Result.success(Unit)

        // When
        useCase(user)

        // Then
        coVerify { repository.addFavorite(user) }
    }
}
