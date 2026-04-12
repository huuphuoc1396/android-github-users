package com.example.github.users.feature.favorites.domain.usecase

import com.example.github.users.core.common.model.UserModel
import com.example.github.users.feature.favorites.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetFavoritesUseCaseImpl @Inject constructor(
    private val repository: FavoriteRepository,
) : GetFavoritesUseCase {
    override operator fun invoke(): Flow<List<UserModel>> = repository.getFavorites()
}
