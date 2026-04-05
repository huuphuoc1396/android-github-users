package com.tyme.github.users.feature.favorites.impl.domain.usecase

import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(username: String) = repository.removeFavorite(username)
}
