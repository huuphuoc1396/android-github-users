package com.example.github.users.feature.favorites.domain.usecase

import com.example.github.users.feature.favorites.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class IsFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoriteRepository,
) : IsFavoriteUseCase {
    override operator fun invoke(username: String): Flow<Boolean> = repository.isFavorite(username)
}
