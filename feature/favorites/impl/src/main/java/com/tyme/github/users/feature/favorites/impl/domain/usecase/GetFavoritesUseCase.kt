package com.tyme.github.users.feature.favorites.impl.domain.usecase

import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    operator fun invoke(): Flow<List<FavoriteUser>> = repository.getFavorites()
}
