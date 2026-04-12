package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import javax.inject.Inject

internal class RemoveFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoriteRepository,
) : RemoveFavoriteUseCase {
    override suspend operator fun invoke(username: String): Result<Unit> = repository.removeFavorite(username)
}
