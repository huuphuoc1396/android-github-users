package com.tyme.github.users.feature.users.domain.usecase

import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {

    operator fun invoke(username: String): Flow<Boolean> {
        return favoriteRepository.isFavorite(username)
    }
}
