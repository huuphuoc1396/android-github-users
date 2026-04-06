package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {

    operator fun invoke(username: String): Flow<Boolean> {
        return favoriteRepository.isFavorite(username)
    }
}
